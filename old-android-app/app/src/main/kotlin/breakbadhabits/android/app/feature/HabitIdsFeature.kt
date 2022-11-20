package breakbadhabits.android.app.feature

import breakbadhabits.android.app.repository.DefaultHabitsRepository
import breakbadhabits.coroutines.ext.flow.mapItems
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class HabitIdsFeature(
    coroutineScope: CoroutineScope,
    habitsRepository: DefaultHabitsRepository
) {

    val state = habitsRepository.habitsFlow().mapItems { habit ->
        habit.id
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), emptyList())

}