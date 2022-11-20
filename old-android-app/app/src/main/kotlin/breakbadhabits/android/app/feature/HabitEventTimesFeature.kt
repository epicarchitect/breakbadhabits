package breakbadhabits.android.app.feature

import breakbadhabits.android.app.repository.DefaultHabitsRepository
import breakbadhabits.coroutines.ext.flow.mapItems
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class HabitEventTimesFeature(
    coroutineScope: CoroutineScope,
    habitsRepository: DefaultHabitsRepository,
    habitId: Int
) {

    val state = habitsRepository.habitEventListByHabitIdFlow(habitId).mapItems {
        it.time
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), emptyList())

}