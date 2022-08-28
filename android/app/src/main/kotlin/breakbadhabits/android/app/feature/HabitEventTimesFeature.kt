package breakbadhabits.android.app.feature

import breakbadhabits.android.app.repository.HabitsRepository
import breakbadhabits.android.app.time.monthEquals
import breakbadhabits.coroutines.flow.mapItems
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.*

class HabitEventTimesFeature(
    coroutineScope: CoroutineScope,
    habitsRepository: HabitsRepository,
    habitId: Int
) {

    val state = habitsRepository.habitEventListByHabitIdFlow(habitId).mapItems {
        it.time
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), emptyList())

}