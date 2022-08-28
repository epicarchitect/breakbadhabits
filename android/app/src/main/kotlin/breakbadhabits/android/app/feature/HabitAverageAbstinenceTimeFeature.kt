package breakbadhabits.android.app.feature

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HabitAverageAbstinenceTimeFeature(
    coroutineScope: CoroutineScope,
    habitAbstinenceTimesFeature: HabitAbstinenceTimesFeature
) {

    val state = habitAbstinenceTimesFeature.state.map {
        if (it.isEmpty()) null
        else it.average().toLong()
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), null)

}