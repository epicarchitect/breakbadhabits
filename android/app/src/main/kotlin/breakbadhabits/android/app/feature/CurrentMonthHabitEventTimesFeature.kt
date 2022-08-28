package breakbadhabits.android.app.feature

import breakbadhabits.android.app.time.monthEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.*

class CurrentMonthHabitEventTimesFeature(
    coroutineScope: CoroutineScope,
    habitEventTimesFeature: HabitEventTimesFeature
) {

    val state = habitEventTimesFeature.state.map {
        it.filter {
            monthEquals(
                Calendar.getInstance().apply { timeInMillis = it },
                Calendar.getInstance()
            )
        }
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), emptyList())

}