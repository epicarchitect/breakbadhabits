package breakbadhabits.android.app.feature

import breakbadhabits.android.app.utils.TikTik
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class HabitTimeSinceFirstEventFeature(
    coroutineScope: CoroutineScope,
    habitEventTimesFeature: HabitEventTimesFeature
) {

    val state = combine(
        habitEventTimesFeature.state,
        TikTik.everySecond()
    ) { times, currentTime ->
        times.minOrNull()?.let { currentTime - it }
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), null)

}