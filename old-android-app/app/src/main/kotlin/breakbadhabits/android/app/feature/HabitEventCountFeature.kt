package breakbadhabits.android.app.feature

import breakbadhabits.android.app.repository.DefaultHabitsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HabitEventCountFeature(
    coroutineScope: CoroutineScope,
    habitsRepository: DefaultHabitsRepository,
    habitId: Int
) {

    val state = habitsRepository.habitEventListByHabitIdFlow(habitId).map { events ->
        events.size
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), 0)

}