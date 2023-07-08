package epicarchitect.breakbadhabits.foundation.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MultiSelectionController<T>(
    coroutineScope: CoroutineScope,
    itemsFlow: Flow<List<T>>
) : StateController<MultiSelectionController.State<T>> {
    private val selectedItems = MutableStateFlow<Set<T>>(emptySet())

    override val state = combine(itemsFlow, selectedItems) { items, selectedItems ->
        State.Loaded(
            items = items,
            selectedItems = selectedItems
        )
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Eagerly,
        initialValue = State.Loading
    )

    fun toggleItems(list: List<T>) {
        selectedItems.value = selectedItems.value.toMutableSet().apply {
            list.forEach { item ->
                val isRemoved = remove(item)
                if (!isRemoved) add(item)
            }
        }
    }

    sealed class State<out T> {
        object Loading : State<Nothing>()
        data class Loaded<T>(
            val items: List<T>,
            val selectedItems: Set<T>
        ) : State<T>()
    }
}

fun <T> MultiSelectionController<T>.toggleItem(item: T) = toggleItems(listOf(item))

fun <T> MultiSelectionController<T>.requireSelectedItems() =
    (state.value as MultiSelectionController.State.Loaded).selectedItems