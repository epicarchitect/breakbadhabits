package epicarchitect.breakbadhabits.monetization

import epicarchitect.breakbadhabits.analytics.PaywallAnalytics
import epicarchitect.breakbadhabits.database.PremiumCache
import epicarchitect.breakbadhabits.database.PremiumLifetimeExpiresAt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import ru.rustore.sdk.core.tasks.Task
import ru.rustore.sdk.pay.RuStorePayClient
import ru.rustore.sdk.pay.model.AcknowledgementState
import ru.rustore.sdk.pay.model.GracePeriod
import ru.rustore.sdk.pay.model.HoldPeriod
import ru.rustore.sdk.pay.model.MainPeriod
import ru.rustore.sdk.pay.model.Product
import ru.rustore.sdk.pay.model.ProductId
import ru.rustore.sdk.pay.model.ProductPurchase
import ru.rustore.sdk.pay.model.ProductPurchaseParams
import ru.rustore.sdk.pay.model.ProductPurchaseStatus
import ru.rustore.sdk.pay.model.PromoPeriod
import ru.rustore.sdk.pay.model.PromotionInfo
import ru.rustore.sdk.pay.model.Purchase
import ru.rustore.sdk.pay.model.PurchaseDiscount
import ru.rustore.sdk.pay.model.PurchaseId
import ru.rustore.sdk.pay.model.RuStorePaymentException
import ru.rustore.sdk.pay.model.SdkTheme
import ru.rustore.sdk.pay.model.SubscriptionPurchase
import ru.rustore.sdk.pay.model.SubscriptionPurchaseStatus
import ru.rustore.sdk.pay.model.TrialPeriod
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.math.roundToInt
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant
import kotlin.time.toKotlinInstant
import epicarchitect.breakbadhabits.format.formatted
import epicarchitect.breakbadhabits.infrastructure.AppDatabase

private const val PURCHASE_SOURCE = "purchase"
private const val TEST_SOURCE = "test"

class RuStoreMonetizationEnvironment : MonetizationEnvironment {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val premiumCacheState = MutableStateFlow<PremiumCache?>(null)
    private var testPremiumGranted = false
    private val dao get() = AppDatabase.premiumCacheDao
    override val store = PurchaseStore.RuStore
    override val purchasesAvailable = true
    override val premiumCache: StateFlow<PremiumCache?> = premiumCacheState.asStateFlow()

    override fun grantTestPremium() {
        testPremiumGranted = true
        premiumCacheState.value = testPremiumCache()
        PaywallAnalytics.setPremium(
            isPremium = true,
            source = TEST_SOURCE,
            sandbox = testPremiumCache().sandbox
        )
    }

    init {
        premiumCacheState.value = runCatching {
            runBlocking(Dispatchers.IO) {
                cachedPremiumCache()
            }
        }.getOrNull()
        val cache = premiumCacheState.value
        PaywallAnalytics.setPremium(
            isPremium = cache?.expiresAt?.let { it > Clock.System.now() } == true,
            source = cache?.source,
            sandbox = cache?.sandbox
        )
        scope.launch {
            refreshPurchases(reportActivation = false)
        }
    }

    override suspend fun loadProducts(): List<PremiumProduct> =
        RuStorePayClient.instance
            .getProductInteractor()
            .getProducts(PremiumProductId.entries.map { ProductId(it.id) })
            .awaitResult()
            .mapNotNull { it.toPremiumProduct() }

    override suspend fun purchase(productId: PremiumProductId, source: String?): PurchaseOutcome {
        val purchaseSource = source ?: PURCHASE_SOURCE
        PaywallAnalytics.purchaseStarted(productId.id)
        return try {
            val purchase = RuStorePayClient.instance
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
                    sdkTheme = SdkTheme.DARK
                )
                .awaitResult()
            refreshPurchases(reportActivation = true, source = purchaseSource)
            val cache = premiumCacheState.value
            PaywallAnalytics.purchaseSucceeded(
                productId = productId.id,
                priceMinorUnits = cache?.priceMinorUnits,
                amountLabel = cache?.amountLabel,
                discountMinorUnits = cache?.discountMinorUnits,
                currency = cache?.currency,
                sandbox = purchase.sandbox
            )
            if (cache?.expiresAt?.let { it > Clock.System.now() } != true) {
                applyCache(
                    cache = PremiumCache(
                        source = purchaseSource,
                        expiresAt = if (purchase.productId.value == PremiumProductId.Lifetime.id) {
                            PremiumLifetimeExpiresAt
                        } else {
                            Clock.System.now() + 1.days
                        },
                        productId = purchase.productId.value,
                        sandbox = purchase.sandbox
                    ),
                    reportActivation = true
                )
            }
            PurchaseOutcome.Success
        } catch (cancelled: RuStorePaymentException.ProductPurchaseCancelled) {
            refreshPurchases(reportActivation = true)
            if (premiumCacheState.value?.expiresAt?.let { it > Clock.System.now() } == true) {
                PurchaseOutcome.Success
            } else {
                PurchaseOutcome.Cancelled
            }
        } catch (throwable: Throwable) {
            PaywallAnalytics.purchaseFailed(productId.id, throwable.message)
            PurchaseOutcome.Failed
        }
    }

    override suspend fun restorePurchases(): RestoreOutcome =
        when (val result = refreshPurchases(reportActivation = true)) {
            PurchaseRefreshResult.Success -> {
                val cache = premiumCacheState.value
                if (cache?.expiresAt?.let { it > Clock.System.now() } == true) {
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

    private suspend fun cachedPremiumCache(): PremiumCache? = dao.get()

    private suspend fun refreshPurchases(
        reportActivation: Boolean,
        source: String = PURCHASE_SOURCE
    ): PurchaseRefreshResult {
        val purchases = try {
            RuStorePayClient.instance
                .getPurchaseInteractor()
                .getPurchases()
                .awaitResult()
        } catch (throwable: Throwable) {
            return PurchaseRefreshResult.Failed(
                reason = throwable.message ?: throwable::class.simpleName
            )
        }

        val acknowledged = acknowledgePurchases(purchases)
        applyCache(
            cache = purchases.toPremiumCache(source, acknowledged),
            reportActivation = reportActivation
        )
        return PurchaseRefreshResult.Success
    }

    private sealed interface PurchaseRefreshResult {
        data object Success : PurchaseRefreshResult
        data class Failed(val reason: String?) : PurchaseRefreshResult
    }

    private suspend fun acknowledgePurchases(purchases: List<Purchase>): Set<PurchaseId> {
        val acknowledged = mutableSetOf<PurchaseId>()
        purchases.forEach { purchase ->
            val acknowledgementState = when (purchase) {
                is ProductPurchase -> purchase.acknowledgementState
                is SubscriptionPurchase -> purchase.acknowledgementState
                else -> null
            }
            if (acknowledgementState == AcknowledgementState.PENDING) {
                runCatching {
                    RuStorePayClient.instance
                        .getPurchaseInteractor()
                        .updateAcknowledgementState(
                            purchaseId = purchase.purchaseId,
                            state = AcknowledgementState.ACKNOWLEDGED,
                            developerPayload = null
                        )
                        .awaitResult()
                    acknowledged += purchase.purchaseId
                }
            }
        }
        return acknowledged
    }

    private suspend fun applyCache(cache: PremiumCache?, reportActivation: Boolean) {
        val effectiveCache = if (testPremiumGranted) {
            testPremiumCache()
        } else {
            cache
        }
        val now = Clock.System.now()
        val wasPremium = premiumCacheState.value?.expiresAt?.let { it > now } == true
        premiumCacheState.value = effectiveCache
        val persistedCache = if (testPremiumGranted) cache else effectiveCache
        runCatching {
            if (persistedCache != null) {
                dao.save(persistedCache)
            } else {
                dao.clear()
            }
        }
        val isPremium = effectiveCache?.expiresAt?.let { it > now } == true
        PaywallAnalytics.setPremium(
            isPremium = isPremium,
            source = effectiveCache?.source,
            sandbox = effectiveCache?.sandbox
        )

        if (reportActivation && isPremium && wasPremium.not()) {
            PaywallAnalytics.premiumActivated(
                source = effectiveCache.source,
                productId = effectiveCache.productId,
                priceMinorUnits = effectiveCache.priceMinorUnits,
                amountLabel = effectiveCache.amountLabel,
                discountMinorUnits = effectiveCache.discountMinorUnits,
                currency = effectiveCache.currency,
                sandbox = effectiveCache.sandbox,
                acknowledgementState = effectiveCache.acknowledgementState
            )
        }
    }

    private fun testPremiumCache() = PremiumCache(
        source = TEST_SOURCE,
        expiresAt = PremiumLifetimeExpiresAt
    )
}

private suspend fun <T : Any> Task<T>.awaitResult(): T =
    suspendCancellableCoroutine { continuation ->
        addOnSuccessListener { result ->
            continuation.resume(result)
        }
        addOnFailureListener { throwable ->
            continuation.resumeWithException(throwable)
        }
        continuation.invokeOnCancellation {
            cancel()
        }
    }

private fun Product.toPremiumProduct(): PremiumProduct? {
    val productId = PremiumProductId.entries.firstOrNull {
        it.id == this.productId.value
    } ?: return null

    val priceMinorUnits = price?.value
    val currencySymbol = amountLabel.value.currencySymbol()

    return PremiumProduct(
        id = productId,
        priceLabel = amountLabel.value,
        monthlyPriceLabel = if (productId == PremiumProductId.Year) monthlyPriceLabel() else null,
        trialDays = if (productId == PremiumProductId.Month) trialPeriodDays() else null,
        priceMinorUnits = priceMinorUnits,
        promotion = promotionInfo?.toPremiumPromotion(
            currentPriceMinorUnits = priceMinorUnits,
            amountLabel = amountLabel.value
        ),
        introOffer = introOffer(currencySymbol),
        gracePeriodDays = subscriptionInfo
            ?.periods
            ?.filterIsInstance<GracePeriod>()
            ?.firstOrNull()
            ?.duration
            ?.isoDurationDays(),
        holdPeriodDays = subscriptionInfo
            ?.periods
            ?.filterIsInstance<HoldPeriod>()
            ?.firstOrNull()
            ?.duration
            ?.isoDurationDays()
    )
}

private fun Product.introOffer(currency: String): PremiumIntroOffer? {
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
        priceLabel = formatMinorUnits(promo.price, currency),
        nextPriceLabel = formatMinorUnits(nextPrice, currency)
    )
}

private fun PromotionInfo.toPremiumPromotion(
    currentPriceMinorUnits: Int?,
    amountLabel: String
): PremiumPromotion? {
    val currentPrice = currentPriceMinorUnits ?: return null
    val basePrice = basePrice.value
    if (basePrice <= currentPrice) return null

    return PremiumPromotion(
        basePriceLabel = formatMinorUnits(
            minorUnits = basePrice,
            currency = amountLabel.currencySymbol()
        ),
        discountLabel = discountValueLabel.value,
        percent = ((1.0 - currentPrice.toDouble() / basePrice) * 100.0)
            .roundToInt()
            .takeIf { it in 1..99 },
        basePriceMinorUnits = basePrice,
        endDate = endDate?.toInstant()?.toKotlinInstant()
    )
}

private fun formatMinorUnits(minorUnits: Int, currency: String): String {
    val rubles = minorUnits / 100
    val kopecks = minorUnits % 100
    val amount = if (kopecks == 0) {
        rubles.formatted()
    } else {
        "${rubles.formatted()},${kopecks.toString().padStart(2, '0')}"
    }
    return if (currency.isEmpty()) amount else "$amount $currency"
}

private fun String.currencySymbol(): String =
    filterNot { it.isDigit() || it.isWhitespace() || it == ',' || it == '.' }.trim()

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
    val kopecks = price?.value ?: return null
    if (currency.value != "RUB") return null

    val rubles = (kopecks / 12.0 / 100.0).roundToInt()
    return "${rubles.formatted()} ₽"
}

private fun List<Purchase>.toPremiumCache(
    source: String,
    acknowledged: Set<PurchaseId>
): PremiumCache? {
    val lifetime = filterIsInstance<ProductPurchase>().firstOrNull {
        it.productId.value == PremiumProductId.Lifetime.id &&
                (it.status == ProductPurchaseStatus.CONFIRMED ||
                        it.status == ProductPurchaseStatus.PAID)
    }

    if (lifetime != null) {
        return lifetime.toPremiumCache(
            source = source,
            productId = lifetime.productId.value,
            expiresAt = PremiumLifetimeExpiresAt,
            discount = lifetime.discount,
            acknowledgementState = lifetime.acknowledgementState.afterAcknowledgement(
                purchaseId = lifetime.purchaseId,
                acknowledged = acknowledged
            )
        )
    }

    val now = Clock.System.now()
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
        purchase.toPremiumCache(
            source = source,
            productId = purchase.productId.value,
            expiresAt = purchase.expirationDate.toInstant().toKotlinInstant(),
            discount = purchase.discount,
            acknowledgementState = purchase.acknowledgementState.afterAcknowledgement(
                purchaseId = purchase.purchaseId,
                acknowledged = acknowledged
            )
        )
    }
}

private fun AcknowledgementState.afterAcknowledgement(
    purchaseId: PurchaseId,
    acknowledged: Set<PurchaseId>
): AcknowledgementState = if (purchaseId in acknowledged) {
    AcknowledgementState.ACKNOWLEDGED
} else {
    this
}

private fun Purchase.toPremiumCache(
    source: String,
    productId: String,
    expiresAt: Instant,
    discount: PurchaseDiscount?,
    acknowledgementState: AcknowledgementState
) = PremiumCache(
    source = source,
    expiresAt = expiresAt,
    productId = productId,
    amountLabel = amountLabel.value,
    priceMinorUnits = price.value,
    discountMinorUnits = discount
        ?.discounts
        ?.takeIf { it.isNotEmpty() }
        ?.sumOf { it.discountValue.value },
    currency = currency.value,
    sandbox = sandbox,
    acknowledgementState = acknowledgementState.name
)
