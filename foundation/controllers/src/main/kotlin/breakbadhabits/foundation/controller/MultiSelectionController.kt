package breakbadhabits.foundation.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

class MultiSelectionController<T>(
    coroutineScope: CoroutineScope,
    itemsFlow: Flow<List<T>>
) : StateController<MultiSelectionController.State<T>> {
    private val selectedMap = MutableStateFlow<Map<T, Boolean>>(emptyMap())

    init {
        itemsFlow.onEach { items ->
            val mutableMap = selectedMap.value.toMutableMap()
            items.forEach {
                if (mutableMap.containsKey(it).not()) {
                    mutableMap[it] = false
                }
            }
            selectedMap.value = mutableMap.toMap()
        }.launchIn(coroutineScope)
    }


    override val state = selectedMap.map {
        State(it)
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Eagerly,
        initialValue = State(selectedMap.value)
    )

    fun toggle(item: T) {
        selectedMap.value = selectedMap.value.toMutableMap().apply {
            val currentValue = get(item) ?: false
            put(item, currentValue.not())

        }.toMap()
    }

    fun checkList(list: List<T>) {
        selectedMap.value = selectedMap.value.toMutableMap().apply {
            list.forEach { item ->
                put(item, true)
            }
        }.toMap()
    }

    data class State<T>(
        val items: Map<T, Boolean>,
    )
}