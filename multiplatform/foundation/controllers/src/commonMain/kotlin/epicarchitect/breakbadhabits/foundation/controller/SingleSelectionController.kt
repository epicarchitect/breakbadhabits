package epicarchitect.breakbadhabits.foundation.controller

import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineScopeOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

class SingleSelectionController<T>(
    override val coroutineScope: CoroutineScope,
    itemsFlow: Flow<List<T>>,
    default: suspend (List<T>) -> T? = { null }
) : Controller<SingleSelectionController.State<T>> {
    private val selected = MutableStateFlow<T?>(null)

    override val state = combine(
        itemsFlow,
        selected
    ) { items, selectedItem ->
        State.Loaded(
            items = items,
            selectedItem = selectedItem ?: default(items)
        )
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Eagerly,
        initialValue = State.Loading
    )

    fun select(item: T) {
        selected.value = item
    }

    sealed class State<out T> {
        object Loading : State<Nothing>()
        data class Loaded<T>(
            val items: List<T>,
            val selectedItem: T?
        ) : State<T>()
    }
}

fun <T> SingleSelectionController(
    coroutineScope: CoroutineScope,
    items: List<T>,
    default: (List<T>) -> T = { it.first() }
) = SingleSelectionController(
    coroutineScope = coroutineScope,
    itemsFlow = flowOf(items),
    default = default
)

fun <T> SingleSelectionController<T>.requireSelectedItem() =
    checkNotNull((state.value as SingleSelectionController.State.Loaded).selectedItem)

fun <T> CoroutineScopeOwner.SingleSelectionController(
    itemsFlow: Flow<List<T>>,
    default: suspend (List<T>) -> T? = { null }
) = SingleSelectionController(
    coroutineScope,
    itemsFlow,
    default
)