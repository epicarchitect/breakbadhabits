package breakbadhabits.android.app.feature

import breakbadhabits.android.app.log
import breakbadhabits.android.app.repository.HabitsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HabitIconIdFeature(
    coroutineScope: CoroutineScope,
    habitsRepository: HabitsRepository,
    habitId: Int
) {

    init { log("init, habitId:$habitId") }

    val state = habitsRepository.habitByIdFlow(habitId).map { habit ->
        habit?.iconId
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), null)

}