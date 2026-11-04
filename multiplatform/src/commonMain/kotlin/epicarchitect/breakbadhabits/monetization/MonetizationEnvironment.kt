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
    suspend fun purchase(productId: PremiumProductId, source: String? = null): PurchaseOutcome
    suspend fun restorePurchases(): RestoreOutcome
    fun grantTestPremium()
}

sealed interface PurchaseOutcome {
    data object Success : PurchaseOutcome
    data object Cancelled : PurchaseOutcome
    data object Failed : PurchaseOutcome
}

sealed interface RestoreOutcome {
    data object Success : RestoreOutcome
    data class Failed(
        val reason: RestoreFailureReason,
        val details: String? = null
    ) : RestoreOutcome
}

enum class RestoreFailureReason {
    NoPurchases,
    StoreError,
    Unknown
}

class PurchasesUnavailableException : IllegalStateException("Purchases are not available")
