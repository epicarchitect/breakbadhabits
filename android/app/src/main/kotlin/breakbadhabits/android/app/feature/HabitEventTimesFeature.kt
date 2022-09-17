package breakbadhabits.android.app.feature

import breakbadhabits.android.app.repository.HabitsRepository
import breakbadhabits.coroutines.flow.mapItems
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class HabitEventTimesFeature(
    coroutineScope: CoroutineScope,
    habitsRepository: HabitsRepository,
    habitId: Int
) {

    val state = habitsRepository.habitEventListByHabitIdFlow(habitId).mapItems {
        it.time
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), emptyList())

}