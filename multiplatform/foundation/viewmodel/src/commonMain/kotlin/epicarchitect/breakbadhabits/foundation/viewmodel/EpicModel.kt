package epicarchitect.breakbadhabits.foundation.viewmodel

import epicarchitect.breakbadhabits.foundation.coroutines.flow.ListStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

//abstract class EpicModel(
//    coroutineScope: CoroutineScope
//) : CoroutineScopeOwnersManager<CoroutineScopeOwner> by CoroutineScopeOwnersManager(coroutineScope)

class EpicContext1<ENVIRONMENT>(
    val environment: ENVIRONMENT
) {
    private val storage = mutableMapOf<Any, Any>()
}

interface EpicContext<ENVIRONMENT> {
    val environment: StateFlow<ENVIRONMENT>
    val storage: StateFlow<Map<Any, Any>>
}

val <ENVIRONMENT> EpicContext<ENVIRONMENT>.currentEnvironment get() = environment.value
val <ENVIRONMENT> EpicContext<ENVIRONMENT>.currentStorage get() = environment.value