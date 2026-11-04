package epicarchitect.breakbadhabits.uikit

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AsyncActionTest {

    @Test
    fun repeatedInvokeIsIgnoredWhileRunning() {
        val gate = CompletableDeferred<Unit>()
        var calls = 0
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)
        val action = AsyncActionImpl(
            coroutineScope = scope,
            onError = {}
        ) {
            calls++
            gate.await()
        }

        action.invoke()
        action.invoke()

        assertTrue(action.isRunning)
        assertEquals(1, calls)

        gate.complete(Unit)
        assertFalse(action.isRunning)
        scope.cancel()
    }

    @Test
    fun actionCanRunAgainAfterCompletion() {
        var calls = 0
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)
        val action = AsyncActionImpl(
            coroutineScope = scope,
            onError = {}
        ) {
            calls++
        }

        action.invoke()
        action.invoke()

        assertEquals(2, calls)
        assertFalse(action.isRunning)
        scope.cancel()
    }

    @Test
    fun actionErrorIsDeliveredToOnErrorAndResetsRunningState() {
        val errors = mutableListOf<Throwable>()
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)
        val action = AsyncActionImpl(
            coroutineScope = scope,
            onError = { errors += it }
        ) {
            error("boom")
        }

        action.invoke()

        assertFalse(action.isRunning)
        assertEquals(1, errors.size)
        assertTrue(errors.single() is IllegalStateException)
        scope.cancel()
    }
}
