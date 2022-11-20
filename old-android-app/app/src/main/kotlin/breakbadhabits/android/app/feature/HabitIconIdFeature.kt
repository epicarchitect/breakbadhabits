package breakbadhabits.android.app.feature

import breakbadhabits.android.app.repository.DefaultHabitsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HabitIconIdFeature(
    coroutineScope: CoroutineScope,
    habitsRepository: DefaultHabitsRepository,
    habitId: Int
) : StateFlow<Int?> by (habitsRepository.habitByIdFlow(habitId).map { habit ->
    habit?.iconId
}.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), null))