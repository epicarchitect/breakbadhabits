package breakbadhabits.android.app.feature

import breakbadhabits.android.app.repository.DefaultHabitsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HabitNameFeature(
    coroutineScope: CoroutineScope,
    habitsRepository: DefaultHabitsRepository,
    habitId: Int
) {

    val state = habitsRepository.habitByIdFlow(habitId).map { habit ->
        habit?.name
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), null)

}