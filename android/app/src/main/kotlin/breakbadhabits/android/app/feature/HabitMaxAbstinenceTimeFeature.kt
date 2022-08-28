package breakbadhabits.android.app.feature

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HabitMaxAbstinenceTimeFeature(
    coroutineScope: CoroutineScope,
    habitAbstinenceTimesFeature: HabitAbstinenceTimesFeature
) {

    val state = habitAbstinenceTimesFeature.state.map(List<Long>::maxOrNull)
        .stateIn(coroutineScope, SharingStarted.WhileSubscribed(), null)

}