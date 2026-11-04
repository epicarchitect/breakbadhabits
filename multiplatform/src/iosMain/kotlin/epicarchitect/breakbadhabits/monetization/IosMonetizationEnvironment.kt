package epicarchitect.breakbadhabits.monetization

import epicarchitect.breakbadhabits.database.PremiumStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class IosMonetizationEnvironment : MonetizationEnvironment {
    private val premiumStatusState = MutableStateFlow<PremiumStatus?>(null)

    override val store = PurchaseStore.Ios
    override val purchasesAvailable = false
    override val premiumStatus: StateFlow<PremiumStatus?> = premiumStatusState.asStateFlow()

    override suspend fun loadProducts(): List<PremiumProduct> =
        throw PurchasesUnavailableException()

    override suspend fun purchase(productId: PremiumProductId): PurchaseOutcome =
        throw PurchasesUnavailableException()

    override suspend fun restorePurchases(): RestoreOutcome =
        throw PurchasesUnavailableException()
}
