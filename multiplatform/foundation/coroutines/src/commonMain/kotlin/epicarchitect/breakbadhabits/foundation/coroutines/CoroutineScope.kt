package epicarchitect.breakbadhabits.foundation.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.job
import kotlinx.coroutines.newCoroutineContext
import kotlin.coroutines.CoroutineContext

fun CoroutineScope.completionBy(
    parentScope: CoroutineScope
) = apply {
    parentScope.invokeOnCompletion {
        cancel(CancellationException("Parent scope completed", it))
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun CoroutineScope.childScope(
    context: CoroutineContext
) = CoroutineScope(
    newCoroutineContext(if (context[Job] != null) context else context + Job())
).completionBy(this)

fun CoroutineScope.invokeOnCompletion(
    handler: CompletionHandler
) = coroutineContext.job.invokeOnCompletion(handler)