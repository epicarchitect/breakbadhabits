package epicarchitect.breakbadhabits.monetization

import android.content.Intent
import epicarchitect.breakbadhabits.database.PremiumCache
import epicarchitect.breakbadhabits.database.PremiumLifetimeExpiresAt
import epicarchitect.breakbadhabits.infrastructure.isDebug
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object PurchasePlatform {
    fun create() = object : MonetizationEnvironment {
        private val premiumCacheState = MutableStateFlow<PremiumCache?>(null)

        override val store = PurchaseStore.GooglePlay
        override val purchasesAvailable = false
        override val premiumCache: StateFlow<PremiumCache?> = premiumCacheState.asStateFlow()

        init {
            if (isDebug) {
//                grantTestPremium()
            }
        }

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

    fun handlePayIntent(intent: Intent?) = Unit
}
