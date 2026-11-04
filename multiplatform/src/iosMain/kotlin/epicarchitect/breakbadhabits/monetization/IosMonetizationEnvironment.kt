package epicarchitect.breakbadhabits.monetization

import epicarchitect.breakbadhabits.database.PremiumCache
import epicarchitect.breakbadhabits.database.PremiumLifetimeExpiresAt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class IosMonetizationEnvironment : MonetizationEnvironment {
    private val premiumCacheState = MutableStateFlow<PremiumCache?>(null)

    override val store = PurchaseStore.Ios
    override val purchasesAvailable = false
    override val premiumCache: StateFlow<PremiumCache?> = premiumCacheState.asStateFlow()

    override suspend fun loadProducts(): List<PremiumProduct> =
        throw PurchasesUnavailableException()

    override suspend fun purchase(productId: PremiumProductId): PurchaseOutcome =
        throw PurchasesUnavailableException()

    override suspend fun restorePurchases(): PurchaseOutcome =
        throw PurchasesUnavailableException()

    override fun grantTestPremium() {
        premiumCacheState.value = PremiumCache(
            source = "test",
            expiresAt = PremiumLifetimeExpiresAt
        )
    }
}
