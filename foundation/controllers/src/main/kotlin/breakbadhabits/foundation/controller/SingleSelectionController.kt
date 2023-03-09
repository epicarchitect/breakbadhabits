package breakbadhabits.foundation.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SingleSelectionController<T>(
    coroutineScope: CoroutineScope,
    items: List<T>,
    default: (List<T>) -> T
) : StateController<SingleSelectionController.State<T>> {
    private val selected = MutableStateFlow(default(items))

    override val state = selected.map {
        State(items, it)
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Eagerly,
        initialValue = State(items, default(items))
    )

    fun select(item: T) {
        selected.value = item
    }

    class State<T>(
        val items: List<T>,
        val selectedItem: T
    )
}