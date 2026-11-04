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
import ru.rustore.sdk.pay.model.Product
import ru.rustore.sdk.pay.model.ProductId
import ru.rustore.sdk.pay.model.ProductPurchase
import ru.rustore.sdk.pay.model.ProductPurchaseParams
import ru.rustore.sdk.pay.model.PromoPeriod
import ru.rustore.sdk.pay.model.ProductPurchaseStatus
import ru.rustore.sdk.pay.model.Purchase
import ru.rustore.sdk.pay.model.RuStorePaymentException
import ru.rustore.sdk.pay.model.SdkTheme
import ru.rustore.sdk.pay.model.SubscriptionPurchase
import ru.rustore.sdk.pay.model.SubscriptionPurchaseStatus
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.math.roundToInt
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.toKotlinInstant
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
        PaywallAnalytics.setPremium(isPremium = true, source = TEST_SOURCE)
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
            source = cache?.source
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

    override suspend fun purchase(productId: PremiumProductId): PurchaseOutcome {
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
            PaywallAnalytics.purchaseSucceeded(productId.id)
            refreshPurchases(reportActivation = true)
            if (premiumCacheState.value?.expiresAt?.let { it > Clock.System.now() } != true) {
                applyCache(
                    cache = PremiumCache(
                        source = PURCHASE_SOURCE,
                        expiresAt = if (purchase.productId.value == PremiumProductId.Lifetime.id) {
                            PremiumLifetimeExpiresAt
                        } else {
                            Clock.System.now() + 1.days
                        }
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

    override suspend fun restorePurchases(): PurchaseOutcome =
        refreshPurchases(reportActivation = true)

    private suspend fun cachedPremiumCache(): PremiumCache? = dao.get()

    private suspend fun refreshPurchases(reportActivation: Boolean): PurchaseOutcome {
        val refreshedCache = try {
            RuStorePayClient.instance
                .getPurchaseInteractor()
                .getPurchases()
                .awaitResult()
                .toPremiumCache()
        } catch (throwable: Throwable) {
            return PurchaseOutcome.Failed
        }

        applyCache(refreshedCache, reportActivation)
        return PurchaseOutcome.Success
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
        PaywallAnalytics.setPremium(isPremium, effectiveCache?.source)

        if (reportActivation && isPremium && wasPremium.not()) {
            PaywallAnalytics.premiumActivated(effectiveCache.source)
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

    return PremiumProduct(
        id = productId,
        priceLabel = amountLabel.value,
        monthlyPriceLabel = if (productId == PremiumProductId.Year) monthlyPriceLabel() else null,
        trialDays = if (productId == PremiumProductId.Year) promoPeriodDays() else null
    )
}

private fun Product.promoPeriodDays(): Int? = subscriptionInfo
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
    return "$rubles ₽"
}

private fun List<Purchase>.toPremiumCache(): PremiumCache? {
    val hasLifetime = filterIsInstance<ProductPurchase>().any {
        it.productId.value == PremiumProductId.Lifetime.id &&
                (it.status == ProductPurchaseStatus.CONFIRMED ||
                        it.status == ProductPurchaseStatus.PAID)
    }

    if (hasLifetime) {
        return PremiumCache(
            source = PURCHASE_SOURCE,
            expiresAt = PremiumLifetimeExpiresAt
        )
    }

    val now = Clock.System.now()
    val activeSubscriptions = filterIsInstance<SubscriptionPurchase>()
        .filter { purchase ->
            purchase.productId.value == PremiumProductId.Month.id ||
                    purchase.productId.value == PremiumProductId.Year.id
        }
        .filter { it.status == SubscriptionPurchaseStatus.ACTIVE }
        .filter { purchase ->
            purchase.expirationDate.toInstant().toKotlinInstant() > now
        }

    if (activeSubscriptions.isNotEmpty()) {
        return PremiumCache(
            source = PURCHASE_SOURCE,
            expiresAt = activeSubscriptions
                .maxOf { it.expirationDate.toInstant().toKotlinInstant() }
        )
    }

    return null
}
