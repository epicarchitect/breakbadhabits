package breakbadhabits.android.app.feature

import breakbadhabits.android.app.repository.HabitsRepository
import breakbadhabits.android.app.time.monthEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.*

class CurrentMonthHabitEventIdsFeature(
    coroutineScope: CoroutineScope,
    habitsRepository: HabitsRepository,
    habitId: Int
) {

    val state = habitsRepository.habitEventListByHabitIdFlow(habitId).map { events ->
        events.filter {
            monthEquals(
                Calendar.getInstance().apply { timeInMillis = it.time },
                Calendar.getInstance()
            )
        }.map { it.id }
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), emptyList())

}