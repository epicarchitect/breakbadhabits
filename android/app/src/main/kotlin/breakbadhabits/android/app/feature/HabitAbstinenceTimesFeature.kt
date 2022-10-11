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
        val sortedEvents = events.sortedBy { it.time }
        List(sortedEvents.size) { i ->
            if (i == sortedEvents.indices.last) {
                currentTime - sortedEvents[i].time
            } else {
                sortedEvents[i + 1].time - sortedEvents[i].time
            }
        }
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), emptyList())

}