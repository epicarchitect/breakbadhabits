package epicarchitect.breakbadhabits.screens.monetization

import epicarchitect.breakbadhabits.database.PremiumStatus
import epicarchitect.breakbadhabits.monetization.MonetizationEnvironment
import epicarchitect.breakbadhabits.monetization.PremiumProduct
import epicarchitect.breakbadhabits.monetization.PremiumProductId
import epicarchitect.breakbadhabits.monetization.PurchaseFailure
import epicarchitect.breakbadhabits.monetization.PurchaseFailureReason
import epicarchitect.breakbadhabits.monetization.PurchaseOutcome
import epicarchitect.breakbadhabits.monetization.PurchaseStore
import epicarchitect.breakbadhabits.monetization.RestoreOutcome
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PaywallScreenStateTest {

    @Test
    fun secondPurchaseTapIsIgnoredWhileFirstIsRunning() {
        val gate = CompletableDeferred<Unit>()
        val monetization = FakeMonetization(
            onPurchase = {
                gate.await()
                PurchaseOutcome.Success
            }
        )
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)
        val state = PaywallScreenState(monetization, scope)

        state.purchase()
        state.purchase()

        assertEquals(1, monetization.purchaseCalls)

        gate.complete(Unit)
        assertEquals(PaywallActionState.Idle, state.actionState)
        scope.cancel()
    }

    @Test
    fun unexpectedPurchaseExceptionBecomesVisibleFailure() {
        val monetization = FakeMonetization(
            onPurchase = { throw IllegalStateException("broken store") }
        )
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)
        val state = PaywallScreenState(monetization, scope)

        state.purchase()

        val failure = state.actionState as PaywallActionState.Failed
        assertEquals(PurchaseFailureReason.Unknown, failure.failure.reason)
        assertEquals("broken store", failure.failure.technicalMessage)
        scope.cancel()
    }

    @Test
    fun applicationNotFoundIsNotHiddenAsIdle() {
        val monetization = FakeMonetization(
            onPurchase = {
                PurchaseOutcome.Failed(
                    PurchaseFailure(
                        reason = PurchaseFailureReason.Unavailable,
                        errorCode = "4000002"
                    )
                )
            }
        )
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)
        val state = PaywallScreenState(monetization, scope)

        state.purchase()

        assertTrue(state.actionState is PaywallActionState.Failed)
        scope.cancel()
    }

    @Test
    fun restoreIsIgnoredWhilePurchaseIsRunning() {
        val gate = CompletableDeferred<Unit>()
        val monetization = FakeMonetization(
            onPurchase = {
                gate.await()
                PurchaseOutcome.Success
            }
        )
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)
        val state = PaywallScreenState(monetization, scope)

        state.purchase()
        state.restorePurchases()

        assertFalse(state.restoreInProgress)
        gate.complete(Unit)
        scope.cancel()
    }

    @Test
    fun purchaseIsIgnoredWhileRestoreIsRunning() {
        val gate = CompletableDeferred<Unit>()
        val monetization = FakeMonetization(
            onRestore = {
                gate.await()
                RestoreOutcome.Success
            }
        )
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)
        val state = PaywallScreenState(monetization, scope)

        state.restorePurchases()
        state.purchase()

        assertTrue(state.restoreInProgress)
        assertEquals(0, monetization.purchaseCalls)
        gate.complete(Unit)
        scope.cancel()
    }

    private class FakeMonetization(
        private val onPurchase: suspend () -> PurchaseOutcome = { PurchaseOutcome.Success },
        private val onRestore: suspend () -> RestoreOutcome = { RestoreOutcome.Success }
    ) : MonetizationEnvironment {
        var purchaseCalls = 0

        override val store = PurchaseStore.RuStore
        override val purchasesAvailable = true
        override val premiumStatus = MutableStateFlow<PremiumStatus?>(null)

        override suspend fun loadProducts(): List<PremiumProduct> = emptyList()

        override suspend fun purchase(productId: PremiumProductId): PurchaseOutcome {
            purchaseCalls++
            return onPurchase()
        }

        override suspend fun restorePurchases(): RestoreOutcome = onRestore()
    }
}
