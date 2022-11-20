package breakbadhabits.android.app.feature

import breakbadhabits.android.app.repository.DefaultHabitsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HabitEventHabitIdFeature(
    coroutineScope: CoroutineScope,
    habitsRepository: DefaultHabitsRepository,
    habitEventId: Int
) {

    val state = habitsRepository.habitEventByIdFlow(habitEventId).map {
        it?.habitId
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), null)

}