package epicarchitect.breakbadhabits.presentation.habits

import epicarchitect.breakbadhabits.di.declaration.logic.DateTimeLogicModule
import epicarchitect.breakbadhabits.di.declaration.logic.HabitsLogicModule
import epicarchitect.breakbadhabits.foundation.controller.DataFlowController
import epicarchitect.breakbadhabits.foundation.controller.SingleRequestController
import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineScopeOwner
import epicarchitect.breakbadhabits.logic.datetime.provider.currentDateTimeFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest

class HabitDetailsViewModel(
    override val coroutineScope: CoroutineScope,
    habitsLogicModule: HabitsLogicModule,
    dateTimeLogicModule: DateTimeLogicModule,
    val habitId: Int
) : CoroutineScopeOwner {

    val habitController = DataFlowController(
        flow = habitsLogicModule.habitProvider.habitFlow(habitId)
    )

    val habitTracksController = DataFlowController(
        flow = habitsLogicModule.habitTrackProvider.habitTracksFlow(habitId)
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentMonthDailyCountsController = DataFlowController(
        flow = dateTimeLogicModule.dateTimeProvider.currentDateTimeFlow().flatMapLatest {
            habitsLogicModule.habitTrackProvider.provideDailyEventCountByRange(
                habitId = habitId,
                range = it..it
            )
        }
    )

    val habitAbstinenceController = DataFlowController(
        flow = habitsLogicModule.habitAbstinenceProvider.currentAbstinenceFlow(habitId)
    )

    val abstinenceListController = DataFlowController(
        flow = habitsLogicModule.habitAbstinenceProvider.abstinenceListFlow(habitId)
    )

    val statisticsController = DataFlowController(
        flow = habitsLogicModule.habitStatisticsProvider.statisticsFlow(habitId)
    )

    val closeController = SingleRequestController()
    val openEditController = SingleRequestController()
    val addTrackController = SingleRequestController()
    val allTracksController = SingleRequestController()
}