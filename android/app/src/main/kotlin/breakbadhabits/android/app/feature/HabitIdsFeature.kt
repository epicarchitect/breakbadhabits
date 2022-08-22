package breakbadhabits.android.app.feature

import breakbadhabits.android.app.log
import breakbadhabits.android.app.repository.HabitsRepository
import breakbadhabits.coroutines.flow.mapItems
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class HabitIdsFeature(
    coroutineScope: CoroutineScope,
    habitsRepository: HabitsRepository
) {

    init { log("init") }

    val state = habitsRepository.habitListFlow().mapItems { habit ->
        habit.id
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), emptyList())

}