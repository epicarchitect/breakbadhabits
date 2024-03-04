package epicarchitect.breakbadhabits.foundation.controller

import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineScopeOwner
import kotlinx.coroutines.flow.StateFlow

interface Controller<DATA> : CoroutineScopeOwner {
    val state: StateFlow<DATA>
}