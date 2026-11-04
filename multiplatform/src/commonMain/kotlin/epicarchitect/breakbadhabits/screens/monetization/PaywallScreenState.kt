package epicarchitect.breakbadhabits.screens.monetization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import epicarchitect.breakbadhabits.infrastructure.Monetization
import epicarchitect.breakbadhabits.monetization.MonetizationEnvironment
import epicarchitect.breakbadhabits.monetization.PremiumProduct
import epicarchitect.breakbadhabits.monetization.PremiumProductId
import epicarchitect.breakbadhabits.monetization.PurchaseFailure
import epicarchitect.breakbadhabits.monetization.PurchaseFailureReason
import epicarchitect.breakbadhabits.monetization.PurchaseOutcome
import epicarchitect.breakbadhabits.monetization.RestoreFailureReason
import epicarchitect.breakbadhabits.monetization.RestoreOutcome
import epicarchitect.breakbadhabits.monetization.PurchaseStore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

sealed interface PaywallProductsState {
    data object Loading : PaywallProductsState
    data class Loaded(val products: List<PremiumProduct>) : PaywallProductsState

    data object Error : PaywallProductsState
}

sealed interface PaywallActionState {
    data object Idle : PaywallActionState
    data object Purchasing : PaywallActionState
    data class Failed(val failure: PurchaseFailure) : PaywallActionState
}

class PaywallScreenState(
    private val monetization: MonetizationEnvironment,
    private val coroutineScope: CoroutineScope,
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
            productsState = try {
                val products = monetization.loadProducts()
                if (products.isEmpty()) {
                    PaywallProductsState.Error
                } else {
                    if (products.none { it.id == selectedProductId }) {
                        selectedProductId = products.first().id
                    }
                    PaywallProductsState.Loaded(products)
                }
            } catch (cancellation: CancellationException) {
                throw cancellation
            } catch (_: Throwable) {
                PaywallProductsState.Error
            }
        }
    }

    fun selectProduct(productId: PremiumProductId) {
        selectedProductId = productId
    }

    fun restorePurchases() {
        if (!purchasesAvailable || restoreInProgress || actionState is PaywallActionState.Purchasing) return
        restoreFlowActive = true
        restoreInProgress = true
        coroutineScope.launch {
            val outcome = try {
                monetization.restorePurchases()
            } catch (cancellation: CancellationException) {
                throw cancellation
            } catch (throwable: Throwable) {
                RestoreOutcome.Failed(
                    reason = RestoreFailureReason.StoreError,
                    details = throwable.message
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

    fun dismissPurchaseError() {
        if (actionState is PaywallActionState.Failed) {
            actionState = PaywallActionState.Idle
        }
    }

    fun purchase() {
        if (!purchasesAvailable ||
            restoreInProgress ||
            actionState is PaywallActionState.Purchasing
        ) {
            return
        }
        val productId = selectedProductId
        actionState = PaywallActionState.Purchasing
        coroutineScope.launch {
            val outcome = try {
                monetization.purchase(productId)
            } catch (cancellation: CancellationException) {
                throw cancellation
            } catch (throwable: Throwable) {
                PurchaseOutcome.Failed(
                    PurchaseFailure(
                        reason = PurchaseFailureReason.Unknown,
                        productId = productId.id,
                        technicalMessage = throwable.message
                    )
                )
            }
            actionState = when (outcome) {
                PurchaseOutcome.Success -> PaywallActionState.Idle

                PurchaseOutcome.Cancelled -> PaywallActionState.Idle
                is PurchaseOutcome.Failed -> PaywallActionState.Failed(outcome.failure)
            }
        }
    }
}

@Composable
fun rememberPaywallScreenState(): PaywallScreenState {
    val coroutineScope = rememberCoroutineScope()
    val state = remember {
        PaywallScreenState(
            monetization = Monetization,
            coroutineScope = coroutineScope
        )
    }
    LaunchedEffect(state) {
        if (Monetization.purchasesAvailable &&
            state.productsState !is PaywallProductsState.Loaded
        ) {
            state.loadProducts()
        }
    }
    return state
}
