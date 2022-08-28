package breakbadhabits.android.app.feature

import breakbadhabits.android.app.repository.HabitsRepository
import breakbadhabits.android.app.time.monthEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.*

class CurrentMonthHabitEventCountFeature(
    coroutineScope: CoroutineScope,
    habitsRepository: HabitsRepository,
    habitId: Int
) {

    val state = habitsRepository.habitEventListByHabitIdFlow(habitId).map { events ->
        events.count {
            monthEquals(
                Calendar.getInstance().apply { timeInMillis = it.time },
                Calendar.getInstance()
            )
        }
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), 0)

}