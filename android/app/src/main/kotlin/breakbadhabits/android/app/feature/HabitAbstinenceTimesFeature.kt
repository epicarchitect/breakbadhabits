package breakbadhabits.android.app.feature

import breakbadhabits.android.app.repository.HabitsRepository
import breakbadhabits.android.app.utils.TikTik
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class HabitAbstinenceTimesFeature(
    coroutineScope: CoroutineScope,
    habitsRepository: HabitsRepository,
    habitId: Int
) {

    val state = combine(
        habitsRepository.habitEventListByHabitIdFlow(habitId),
        TikTik.everySecond()
    ) { events, currentTime ->
        List(events.size) { i ->
            if (i != events.indices.last) {
                events[i + 1].time - events[i].time
            } else {
                currentTime - events[i].time
            }
        }
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), emptyList())

}