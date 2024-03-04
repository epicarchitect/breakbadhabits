package epicarchitect.breakbadhabits.foundation.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

interface CoroutineScopeOwner {
    val coroutineScope: CoroutineScope
}

fun CoroutineScopeOwner.cancel(
    cause: CancellationException? = null
) = coroutineScope.cancel(cause)

