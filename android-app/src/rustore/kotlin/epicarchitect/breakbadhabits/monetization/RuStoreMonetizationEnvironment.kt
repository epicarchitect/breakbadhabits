package epicarchitect.breakbadhabits.monetization

import epicarchitect.breakbadhabits.database.PremiumStatus
import epicarchitect.breakbadhabits.monetization.isActive
import epicarchitect.breakbadhabits.format.formatPriceMajorUnits
import epicarchitect.breakbadhabits.format.formatPriceMinorUnits
import epicarchitect.breakbadhabits.infrastructure.AppDatabase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import ru.rustore.sdk.core.tasks.Task
import ru.rustore.sdk.pay.RuStorePayClient
import ru.rustore.sdk.pay.callback.PurchaseEventListener
import ru.rustore.sdk.pay.model.AcknowledgementState
import ru.rustore.sdk.pay.model.InvoiceId
import ru.rustore.sdk.pay.model.MainPeriod
import ru.rustore.sdk.pay.model.Product
import ru.rustore.sdk.pay.model.ProductId
import ru.rustore.sdk.pay.model.ProductPurchase
import ru.rustore.sdk.pay.model.ProductPurchaseParams
import ru.rustore.sdk.pay.model.ProductPurchaseStatus
import ru.rustore.sdk.pay.model.PromoPeriod
import ru.rustore.sdk.pay.model.PromotionInfo
import ru.rustore.sdk.pay.model.Purchase
import ru.rustore.sdk.pay.model.PurchaseId
import ru.rustore.sdk.pay.model.RuStorePaymentException
import ru.rustore.sdk.pay.model.SdkTheme
import ru.rustore.sdk.pay.model.SubscriptionPurchase
import ru.rustore.sdk.pay.model.SubscriptionPurchaseStatus
import ru.rustore.sdk.pay.model.TrialPeriod
import kotlin.coroutines.resumeWithException
import kotlin.math.roundToInt
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant
import kotlin.time.toKotlinInstant

class RuStoreMonetizationEnvironment : MonetizationEnvironment {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val premiumStatusState = MutableStateFlow<PremiumStatus?>(null)
    override val store = PurchaseStore.RuStore
    override val purchasesAvailable = true
    override val premiumStatus: StateFlow<PremiumStatus?> = premiumStatusState.asStateFlow()

    init {
        scope.launch {
            premiumStatusState.value = runCatching {
                cachedPremiumStatus()
            }.getOrNull()
            refreshPurchases()
        }
    }

    override suspend fun loadProducts(): List<PremiumProduct> =
        RuStorePayClient.instance
            .getProductInteractor()
            .getProducts(PremiumProductId.entries.map { ProductId(it.id) })
            .awaitResult()
            .mapNotNull { it.toPremiumProduct() }

    override suspend fun purchase(productId: PremiumProductId): PurchaseOutcome {
        val cancellationTracker = PurchaseCancellationTracker()
        return try {
            RuStorePayClient.instance
                .getPurchaseInteractor()
                .purchase(
                    params = ProductPurchaseParams(
                        ProductId(productId.id),
                        null,
                        null,
                        null,
                        null,
                        null
                    ),
                    sdkTheme = SdkTheme.DARK,
                    purchaseEventListener = cancellationTracker
                )
                .awaitResult(timeout = 15.minutes)
            refreshPurchases()
            val status = premiumStatusState.value
            if (!status.isActive(Clock.System.now())) {
                applyStatus(
                    fallbackPremiumStatus(
                        productId = productId,
                        now = Clock.System.now()
                    )
                )
            }
            PurchaseOutcome.Success
        } catch (cancelled: RuStorePaymentException.ProductPurchaseCancelled) {
            cancelledOutcome()
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (throwable: Throwable) {
            if (cancellationTracker.cancelled) {
                cancelledOutcome()
            } else {
                PurchaseOutcome.Failed(throwable.toPurchaseFailure(productId.id))
            }
        }
    }

    private suspend fun cancelledOutcome(): PurchaseOutcome {
        refreshPurchases()
        return if (premiumStatusState.value.isActive(Clock.System.now())) {
            PurchaseOutcome.Success
        } else {
            PurchaseOutcome.Cancelled
        }
    }

    override suspend fun restorePurchases(): RestoreOutcome =
        when (val result = refreshPurchases()) {
            PurchaseRefreshResult.Success -> {
                val status = premiumStatusState.value
                if (status.isActive(Clock.System.now())) {
                    RestoreOutcome.Success
                } else {
                    RestoreOutcome.Failed(RestoreFailureReason.NoPurchases)
                }
            }

            is PurchaseRefreshResult.Failed -> RestoreOutcome.Failed(
                reason = RestoreFailureReason.StoreError,
                details = result.reason
            )
        }

    private suspend fun cachedPremiumStatus(): PremiumStatus? = AppDatabase.premiumStatusDao.get()

    private suspend fun refreshPurchases(): PurchaseRefreshResult {
        val purchases = try {
            RuStorePayClient.instance
                .getPurchaseInteractor()
                .getPurchases()
                .awaitResult()
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (throwable: Throwable) {
            return PurchaseRefreshResult.Failed(
                reason = throwable.message ?: throwable::class.simpleName
            )
        }

        acknowledgePurchases(purchases)
        val now = Clock.System.now()
        applyStatus(
            status = purchases.toPremiumStatus(now)
        )
        return PurchaseRefreshResult.Success
    }

    private sealed interface PurchaseRefreshResult {
        data object Success : PurchaseRefreshResult
        data class Failed(val reason: String?) : PurchaseRefreshResult
    }

    private suspend fun acknowledgePurchases(purchases: List<Purchase>) {
        purchases.forEach { purchase ->
            val acknowledgementState = when (purchase) {
                is ProductPurchase -> purchase.acknowledgementState
                is SubscriptionPurchase -> purchase.acknowledgementState
                else -> null
            }
            if (acknowledgementState == AcknowledgementState.PENDING) {
                try {
                    RuStorePayClient.instance
                        .getPurchaseInteractor()
                        .updateAcknowledgementState(
                            purchaseId = purchase.purchaseId,
                            state = AcknowledgementState.ACKNOWLEDGED,
                            developerPayload = null
                        )
                        .awaitResult()
                } catch (cancellation: CancellationException) {
                    throw cancellation
                } catch (_: Throwable) {
                }
            }
        }
    }

    private suspend fun applyStatus(status: PremiumStatus?) {
        premiumStatusState.value = status
        try {
            if (status != null) {
                AppDatabase.premiumStatusDao.save(status)
            } else {
                AppDatabase.premiumStatusDao.clear()
            }
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (_: Throwable) {
        }
    }
}

private class PurchaseCancellationTracker : PurchaseEventListener {
    @Volatile
    var cancelled = false

    override fun onPurchaseCancelled(purchaseId: PurchaseId?, invoiceId: InvoiceId?) {
        cancelled = true
    }

    override fun onPurchaseCreated(purchaseId: PurchaseId, invoiceId: InvoiceId) = Unit

    override fun onPaymentStarted(purchaseId: PurchaseId, invoiceId: InvoiceId) = Unit

    override fun onPaymentCompleted(purchaseId: PurchaseId, invoiceId: InvoiceId) = Unit

    override fun onPaymentFailed(purchaseId: PurchaseId?, invoiceId: InvoiceId?) = Unit
}

private fun Throwable.toPurchaseFailure(productId: String): PurchaseFailure {
    val purchaseException = findCause<RuStorePaymentException.ProductPurchaseException>()
    val networkException = findCause<RuStorePaymentException.RuStorePaymentNetworkException>()

    return PurchaseFailure(
        reason = purchaseFailureReason(),
        productId = purchaseException?.productId?.value ?: productId,
        purchaseId = purchaseException?.purchaseId?.value,
        orderId = purchaseException?.orderId?.value,
        invoiceId = purchaseException?.invoiceId?.value,
        errorCode = networkException?.code?.takeIf { it.isNotBlank() }
            ?: this::class.simpleName,
        technicalMessage = purchaseException?.cause?.message?.takeIf { it.isNotBlank() }
            ?: message?.takeIf { it.isNotBlank() }
    )
}

private fun Throwable.purchaseFailureReason(): PurchaseFailureReason {
    purchaseErrorCode()?.let { return it.purchaseFailureReason() }

    return when {
        findCause<RuStorePaymentException.RuStorePaymentNetworkException>() != null ->
            PurchaseFailureReason.Network

        findCause<RuStorePaymentException.RuStorePayInvalidActivePurchase>() != null ->
            PurchaseFailureReason.AlreadyActive

        findCause<RuStorePaymentException.RuStorePayInvalidConsoleAppId>() != null ->
            PurchaseFailureReason.Unavailable

        findCause<RuStorePaymentException.RuStorePaySignatureException>() != null ||
                findCause<RuStorePaymentException.RuStorePayClientNotCreated>() != null ||
                findCause<RuStorePaymentException.RuStorePayClientAlreadyExist>() != null ||
                findCause<RuStorePaymentException.ApplicationSchemeWasNotProvided>() != null ->
            PurchaseFailureReason.Unavailable

        findCause<RuStorePaymentException.ProductPurchaseException>() != null ->
            PurchaseFailureReason.PaymentDeclined

        findCause<RuStorePaymentException.EmptyPaymentTokenException>() != null ||
                findCause<RuStorePaymentException.InvalidCardBindingIdException>() != null ->
            PurchaseFailureReason.PaymentDeclined

        else -> PurchaseFailureReason.Unknown
    }
}

private fun Throwable.purchaseErrorCode(): String? =
    findCause<RuStorePaymentException.RuStorePaymentNetworkException>()
        ?.code
        ?.takeIf { it.isNotBlank() }

private fun String.purchaseFailureReason(): PurchaseFailureReason = when {
    this in setOf("4000002", "4000016", "4040005") ->
        PurchaseFailureReason.Unavailable

    this in setOf("4000003", "4030002") ->
        PurchaseFailureReason.Blocked

    this in setOf("4000020", "4000021", "4000041") ->
        PurchaseFailureReason.AlreadyActive

    this == "4000025" ->
        PurchaseFailureReason.NoPaymentMethod

    startsWith("401") ->
        PurchaseFailureReason.SignInRequired

    this in setOf("4040002", "4040003", "4040004") ->
        PurchaseFailureReason.PaymentSystem

    startsWith("5") ->
        PurchaseFailureReason.Unavailable

    else -> PurchaseFailureReason.Unknown
}

private inline fun <reified T : Throwable> Throwable.findCause(): T? =
    generateSequence(this) { it.cause }.filterIsInstance<T>().firstOrNull()

private class PurchaseTimeoutException : RuntimeException("RuStore operation timed out")

private suspend fun <T : Any> Task<T>.awaitResult(
    timeout: Duration = 30.seconds
): T {
    val result = withTimeoutOrNull(timeout) {
        suspendCancellableCoroutine { continuation ->
            addOnSuccessListener { value ->
                continuation.resume(value) { _, _, _ -> }
            }
            addOnFailureListener { throwable ->
                continuation.resumeWithException(throwable)
            }
            continuation.invokeOnCancellation {
                cancel()
            }
        }
    }
    return result ?: throw PurchaseTimeoutException()
}

private fun Product.toPremiumProduct(): PremiumProduct? {
    val productId = PremiumProductId.entries.firstOrNull {
        it.id == this.productId.value
    } ?: return null

    val priceMinorUnits = price?.value
    val currencyCode = currency.value

    return PremiumProduct(
        id = productId,
        priceLabel = priceMinorUnits
            ?.let { formatPriceMinorUnits(it, currencyCode) }
            ?: amountLabel.value.toPriceLabel(currencyCode),
        monthlyPriceLabel = if (productId == PremiumProductId.Year) monthlyPriceLabel() else null,
        trialDays = if (productId == PremiumProductId.Month) trialPeriodDays() else null,
        priceMinorUnits = priceMinorUnits,
        currencyCode = currencyCode,
        promotion = promotionInfo?.toPremiumPromotion(
            currentPriceMinorUnits = priceMinorUnits,
            currencyCode = currencyCode
        ),
        introOffer = introOffer(currencyCode)
    )
}

private fun Product.introOffer(currencyCode: String): PremiumIntroOffer? {
    val promo = subscriptionInfo
        ?.periods
        ?.filterIsInstance<PromoPeriod>()
        ?.firstOrNull { it.price > 0 }
        ?: return null
    val nextPrice = subscriptionInfo
        ?.periods
        ?.filterIsInstance<MainPeriod>()
        ?.firstOrNull()
        ?.price
        ?: price?.value
        ?: return null
    if (promo.price >= nextPrice) return null

    return PremiumIntroOffer(
        priceLabel = formatPriceMinorUnits(promo.price, currencyCode),
        nextPriceLabel = formatPriceMinorUnits(nextPrice, currencyCode)
    )
}

private fun PromotionInfo.toPremiumPromotion(
    currentPriceMinorUnits: Int?,
    currencyCode: String
): PremiumPromotion? {
    val currentPrice = currentPriceMinorUnits ?: return null
    val basePrice = basePrice.value
    if (basePrice <= currentPrice) return null

    return PremiumPromotion(
        basePriceLabel = formatPriceMinorUnits(basePrice, currencyCode),
        percent = ((1.0 - currentPrice.toDouble() / basePrice) * 100.0)
            .roundToInt()
            .takeIf { it in 1..99 },
        basePriceMinorUnits = basePrice,
        endDate = endDate?.toInstant()?.toKotlinInstant()
    )
}

private fun String.toPriceLabel(currencyCode: String): String {
    val match = Regex("""(\d[\d\s\u00A0]*)(?:[.,](\d{1,2}))?""").find(this) ?: return this
    val majorUnits = match.groupValues[1]
        .filter(Char::isDigit)
        .toIntOrNull()
        ?: return this
    val kopecks = match.groupValues[2]
        .padEnd(2, '0')
        .toIntOrNull()
        ?: 0

    return formatPriceMinorUnits(majorUnits * 100 + kopecks, currencyCode)
}

private fun Product.trialPeriodDays(): Int? = subscriptionInfo
    ?.periods
    ?.filterIsInstance<TrialPeriod>()
    ?.firstOrNull()
    ?.duration
    ?.isoDurationDays()
    ?: subscriptionInfo
        ?.periods
        ?.filterIsInstance<PromoPeriod>()
        ?.firstOrNull { it.price == 0 }
        ?.duration
        ?.isoDurationDays()

private fun String.isoDurationDays(): Int? {
    toIntOrNull()?.let { return it.takeIf { days -> days > 0 } }

    val match = Regex("""P(?:(\d+)Y)?(?:(\d+)M)?(?:(\d+)W)?(?:(\d+)D)?""")
        .matchEntire(this)
        ?: return null

    val years = match.groupValues[1].toIntOrNull() ?: 0
    val months = match.groupValues[2].toIntOrNull() ?: 0
    val weeks = match.groupValues[3].toIntOrNull() ?: 0
    val days = match.groupValues[4].toIntOrNull() ?: 0
    val totalDays = years * 365 + months * 30 + weeks * 7 + days

    return totalDays.takeIf { it > 0 }
}

private fun Product.monthlyPriceLabel(): String? {
    val monthlyMinorUnits = price?.value ?: return null
    val monthlyMajorUnits = (monthlyMinorUnits / 12.0 / 100.0).roundToInt()
    return formatPriceMajorUnits(monthlyMajorUnits, currency.value)
}

private fun List<Purchase>.toPremiumStatus(now: Instant): PremiumStatus? {
    val lifetime = filterIsInstance<ProductPurchase>().firstOrNull {
        it.productId.value == PremiumProductId.Lifetime.id &&
                (it.status == ProductPurchaseStatus.CONFIRMED ||
                        it.status == ProductPurchaseStatus.PAID)
    }

    if (lifetime != null) {
        return PremiumStatus(
            expiresAt = Instant.DISTANT_FUTURE
        )
    }

    val activeSubscription = filterIsInstance<SubscriptionPurchase>()
        .filter { purchase ->
            purchase.productId.value == PremiumProductId.Month.id ||
                    purchase.productId.value == PremiumProductId.Year.id
        }
        .filter { purchase ->
            val isUnexpired = purchase.expirationDate.toInstant().toKotlinInstant() > now
            isUnexpired && (
                    purchase.status == SubscriptionPurchaseStatus.ACTIVE ||
                            (purchase.status == SubscriptionPurchaseStatus.PAUSED &&
                                    purchase.gracePeriodEnabled)
                    )
        }
        .maxByOrNull { it.expirationDate.toInstant().toKotlinInstant() }

    return activeSubscription?.let { purchase ->
        PremiumStatus(
            expiresAt = purchase.expirationDate.toInstant().toKotlinInstant()
        )
    }
}
