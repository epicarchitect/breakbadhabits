package epicarchitect.breakbadhabits.monetization

import epicarchitect.breakbadhabits.database.PremiumCache
import kotlinx.coroutines.flow.StateFlow

const val RU_STORE_APP_URL =
    "https://www.rustore.ru/catalog/app/kolmachikhin.alexander.breakbadhabits"

enum class PurchaseStore {
    RuStore,
    GooglePlay,
    Ios
}

interface MonetizationEnvironment {
    val store: PurchaseStore
    val purchasesAvailable: Boolean
    val premiumCache: StateFlow<PremiumCache?>

    suspend fun loadProducts(): List<PremiumProduct>
    suspend fun purchase(productId: PremiumProductId): PurchaseOutcome
    suspend fun restorePurchases(): PurchaseOutcome
    fun grantTestPremium()
}

sealed interface PurchaseOutcome {
    data object Success : PurchaseOutcome
    data object Cancelled : PurchaseOutcome
    data object Failed : PurchaseOutcome
}

class PurchasesUnavailableException : IllegalStateException("Purchases are not available")
