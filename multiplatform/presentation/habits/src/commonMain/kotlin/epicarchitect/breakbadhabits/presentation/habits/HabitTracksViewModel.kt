package epicarchitect.breakbadhabits.presentation.habits

import epicarchitect.breakbadhabits.foundation.controller.DataFlowController
import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineScopeOwner
import epicarchitect.breakbadhabits.logic.habits.provider.HabitProvider
import epicarchitect.breakbadhabits.logic.habits.provider.HabitTrackProvider
import kotlinx.coroutines.CoroutineScope

class HabitTracksViewModel(
    override val coroutineScope: CoroutineScope,
    habitProvider: HabitProvider,
    habitTrackProvider: HabitTrackProvider,
    habitId: Int
) : CoroutineScopeOwner {

    val habitController = DataFlowController(
        flow = habitProvider.habitFlow(habitId)
    )

    val habitTracksController = DataFlowController(
        flow = habitTrackProvider.monthsToHabitTracksFlow(habitId)
    )
}