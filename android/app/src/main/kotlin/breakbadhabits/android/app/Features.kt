package breakbadhabits.android.app

import breakbadhabits.android.app.repository.HabitsRepository
import breakbadhabits.android.app.utils.TikTik
import breakbadhabits.coroutines.flow.mapItems
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
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

class HabitIconIdFeature(
    coroutineScope: CoroutineScope,
    habitsRepository: HabitsRepository,
    habitId: Int
) {

    init { log("init, habitId:$habitId") }

    val state = habitsRepository.habitByIdFlow(habitId).map { habit ->
        habit?.iconId
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), null)

}


class HabitIdsFeature(
    coroutineScope: CoroutineScope,
    habitsRepository: HabitsRepository
) {

    init { log("init") }

    val state = habitsRepository.habitListFlow().mapItems { habit ->
        habit.id
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), emptyList())

}

class HabitNameFeature(
    coroutineScope: CoroutineScope,
    habitsRepository: HabitsRepository,
    habitId: Int
) {

    init { log("init, habitId:$habitId") }

    val state = habitsRepository.habitByIdFlow(habitId).map { habit ->
        habit?.name
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), null)

}