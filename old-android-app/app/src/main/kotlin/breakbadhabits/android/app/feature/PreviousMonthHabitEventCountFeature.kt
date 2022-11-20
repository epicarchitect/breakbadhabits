package breakbadhabits.android.app.feature

import breakbadhabits.android.app.repository.DefaultHabitsRepository
import breakbadhabits.android.app.time.monthEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.*

class PreviousMonthHabitEventCountFeature(
    coroutineScope: CoroutineScope,
    habitsRepository: DefaultHabitsRepository,
    habitId: Int
) {

    val state = habitsRepository.habitEventListByHabitIdFlow(habitId).map { events ->
        events.count {
            monthEquals(
                Calendar.getInstance().apply {
                    timeInMillis = it.time
                },
                Calendar.getInstance().apply {
                    if (get(Calendar.MONTH) - 1 == -1) {
                        set(Calendar.MONTH, 11)
                        set(Calendar.YEAR, get(Calendar.YEAR) - 1)
                    } else {
                        set(Calendar.MONTH, get(Calendar.MONTH) - 1)
                    }

                    set(Calendar.DAY_OF_MONTH, 1)
                }
            )
        }
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), 0)

}