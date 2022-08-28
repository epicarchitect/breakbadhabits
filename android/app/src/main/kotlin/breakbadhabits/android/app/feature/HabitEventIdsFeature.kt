package breakbadhabits.android.app.feature

import breakbadhabits.android.app.repository.HabitsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class HabitEventIdsFeature(
    coroutineScope: CoroutineScope,
    habitsRepository: HabitsRepository,
    habitId: Int
) {

    private val timeFilter = MutableStateFlow<(Long) -> Boolean> { true }

    val state = combine(
        timeFilter,
        habitsRepository.habitEventListByHabitIdFlow(habitId)
    ) { timeFilter, events ->
        events.filter { timeFilter(it.time) }.map { it.id }
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), emptyList())

    fun setTimeFilter(predicate: (Long) -> Boolean) {
        timeFilter.update { predicate }
    }
}