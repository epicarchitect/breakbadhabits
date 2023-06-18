package epicarchitect.breakbadhabits.foundation.controller

import kotlinx.coroutines.flow.StateFlow

interface StateController<DATA> {
    val state: StateFlow<DATA>
}
