package epicarchitect.breakbadhabits.monetization

import epicarchitect.breakbadhabits.database.PremiumStatus
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
    val premiumStatus: StateFlow<PremiumStatus?>

    suspend fun loadProducts(): List<PremiumProduct>
    suspend fun purchase(productId: PremiumProductId): PurchaseOutcome
    suspend fun restorePurchases(): RestoreOutcome
}

sealed interface PurchaseOutcome {
    data object Success : PurchaseOutcome
    data object Cancelled : PurchaseOutcome
    data class Failed(val failure: PurchaseFailure) : PurchaseOutcome
}

data class PurchaseFailure(
    val reason: PurchaseFailureReason,
    val productId: String? = null,
    val purchaseId: String? = null,
    val orderId: String? = null,
    val invoiceId: String? = null,
    val errorCode: String? = null,
    val technicalMessage: String? = null
)

enum class PurchaseFailureReason {
    Network,
    PaymentDeclined,
    Unavailable,
    AlreadyActive,
    NoPaymentMethod,
    SignInRequired,
    Blocked,
    PaymentSystem,
    Unknown
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
