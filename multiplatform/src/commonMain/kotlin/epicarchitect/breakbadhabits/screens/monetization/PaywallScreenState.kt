package epicarchitect.breakbadhabits.screens.monetization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import epicarchitect.breakbadhabits.analytics.PaywallAnalytics
import epicarchitect.breakbadhabits.infrastructure.Monetization
import epicarchitect.breakbadhabits.infrastructure.isDebug
import epicarchitect.breakbadhabits.monetization.MonetizationEnvironment
import epicarchitect.breakbadhabits.monetization.PremiumProduct
import epicarchitect.breakbadhabits.monetization.PremiumProductId
import epicarchitect.breakbadhabits.monetization.PurchaseOutcome
import epicarchitect.breakbadhabits.monetization.RestoreFailureReason
import epicarchitect.breakbadhabits.monetization.RestoreOutcome
import epicarchitect.breakbadhabits.monetization.PurchaseStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

sealed interface PaywallProductsState {
    data object Loading : PaywallProductsState
    data class Loaded(
        val products: List<PremiumProduct>,
        val isDebugStub: Boolean = false
    ) : PaywallProductsState

    data object Error : PaywallProductsState
}

private val DebugPremiumProducts = listOf(
    PremiumProduct(
        id = PremiumProductId.Year,
        priceLabel = "2 990 ₽",
        monthlyPriceLabel = "249 ₽",
        priceMinorUnits = 299_000
    ),
    PremiumProduct(
        id = PremiumProductId.Month,
        priceLabel = "490 ₽",
        trialDays = 3,
        priceMinorUnits = 49_000
    ),
    PremiumProduct(
        id = PremiumProductId.Lifetime,
        priceLabel = "5 990 ₽",
        priceMinorUnits = 599_000
    )
)

sealed interface PaywallActionState {
    data object Idle : PaywallActionState
    data object Purchasing : PaywallActionState
    data class Message(val message: PaywallMessage) : PaywallActionState
}

enum class PaywallMessage(val isError: Boolean) {
    PurchaseCancelled(isError = false),
    PurchaseFailed(isError = true)
}

class PaywallScreenState(
    private val monetization: MonetizationEnvironment,
    private val coroutineScope: CoroutineScope,
    val source: String,
    initialProductsState: PaywallProductsState = PaywallProductsState.Loading
) {
    var productsState by mutableStateOf(initialProductsState)
        private set
    var selectedProductId by mutableStateOf(PremiumProductId.Year)
        private set
    val purchasesAvailable: Boolean
        get() = monetization.purchasesAvailable
    val store: PurchaseStore
        get() = monetization.store
    var actionState by mutableStateOf<PaywallActionState>(PaywallActionState.Idle)
        private set
    var restoreOutcome by mutableStateOf<RestoreOutcome?>(null)
        private set
    var restoreInProgress by mutableStateOf(false)
        private set
    var restoreFlowActive by mutableStateOf(false)
        private set

    fun loadProducts() {
        productsState = PaywallProductsState.Loading
        coroutineScope.launch {
            productsState = runCatching { monetization.loadProducts() }
                .fold(
                    onSuccess = { products ->
                        if (products.isEmpty()) {
                            debugStubOrError()
                        } else {
                            if (products.none { it.id == selectedProductId }) {
                                selectedProductId = products.first().id
                            }
                            PaywallProductsState.Loaded(products)
                        }
                    },
                    onFailure = { debugStubOrError() }
                )
        }
    }

    private fun debugStubOrError(): PaywallProductsState =
        if (isDebug) {
            PaywallProductsState.Loaded(
                products = DebugPremiumProducts,
                isDebugStub = true
            )
        } else {
            PaywallProductsState.Error
        }

    fun selectProduct(productId: PremiumProductId) {
        selectedProductId = productId
        PaywallAnalytics.paywallProductSelected(productId.id)
    }

    fun restorePurchases() {
        if (!purchasesAvailable || restoreInProgress) return
        PaywallAnalytics.paywallRestoreClicked()
        restoreFlowActive = true
        restoreInProgress = true
        coroutineScope.launch {
            val outcome = runCatching { monetization.restorePurchases() }
                .getOrElse {
                    RestoreOutcome.Failed(
                        reason = RestoreFailureReason.StoreError,
                        details = it.message
                    )
                }
            restoreInProgress = false
            restoreOutcome = outcome
        }
    }

    fun dismissRestoreOutcome() {
        restoreOutcome = null
        restoreFlowActive = false
    }

    fun purchase() {
        val productId = selectedProductId
        PaywallAnalytics.paywallCtaClicked(productId.id)
        val productsState = productsState
        if (productsState is PaywallProductsState.Loaded && productsState.isDebugStub) {
            monetization.grantTestPremium()
            PaywallAnalytics.premiumActivated("debug_stub")
            actionState = PaywallActionState.Idle
            return
        }
        actionState = PaywallActionState.Purchasing
        coroutineScope.launch {
            actionState = when (monetization.purchase(productId, source)) {
                PurchaseOutcome.Success -> PaywallActionState.Idle

                PurchaseOutcome.Cancelled ->
                    PaywallActionState.Message(PaywallMessage.PurchaseCancelled)

                PurchaseOutcome.Failed ->
                    PaywallActionState.Message(PaywallMessage.PurchaseFailed)
            }
        }
    }
}

@Composable
fun rememberPaywallScreenState(source: String): PaywallScreenState {
    val coroutineScope = rememberCoroutineScope()
    val state = remember(source) {
        PaywallScreenState(
            monetization = Monetization,
            coroutineScope = coroutineScope,
            source = source
        )
    }
    LaunchedEffect(state) {
        PaywallAnalytics.paywallViewed(source)
        if (Monetization.purchasesAvailable &&
            state.productsState !is PaywallProductsState.Loaded
        ) {
            state.loadProducts()
        }
    }
    return state
}
