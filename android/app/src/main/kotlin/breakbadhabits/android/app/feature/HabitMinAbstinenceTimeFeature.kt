package breakbadhabits.android.app.feature

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HabitMinAbstinenceTimeFeature(
    coroutineScope: CoroutineScope,
    habitAbstinenceTimesFeature: HabitAbstinenceTimesFeature
) {

    val state = habitAbstinenceTimesFeature.state.map { it.dropLast(1).minOrNull() }
        .stateIn(coroutineScope, SharingStarted.WhileSubscribed(), null)

}