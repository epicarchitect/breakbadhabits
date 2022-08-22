package breakbadhabits.android.app.feature

import breakbadhabits.android.app.log
import breakbadhabits.android.app.repository.HabitsRepository
import breakbadhabits.android.app.utils.TikTik
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class HabitAbstinenceTimeFeature(
    coroutineScope: CoroutineScope,
    habitsRepository: HabitsRepository,
    habitId: Int
) {

    init { log("init, habitId:$habitId") }

    val state = combine(
        habitsRepository.lastByTimeHabitEventByHabitIdFlow(habitId),
        TikTik.everySecond()
    ) { event, currentTime ->
        event?.let { currentTime - it.time }
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), null)

}