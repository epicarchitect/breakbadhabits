package epicarchitect.breakbadhabits.presentation.habits

import epicarchitect.breakbadhabits.foundation.controller.LoadingController
import epicarchitect.breakbadhabits.foundation.viewmodel.ViewModel
import epicarchitect.breakbadhabits.logic.datetime.provider.DateTimeProvider
import epicarchitect.breakbadhabits.logic.datetime.provider.currentDateTimeFlow
import epicarchitect.breakbadhabits.logic.habits.provider.HabitAbstinenceProvider
import epicarchitect.breakbadhabits.logic.habits.provider.HabitProvider
import epicarchitect.breakbadhabits.logic.habits.provider.HabitStatisticsProvider
import epicarchitect.breakbadhabits.logic.habits.provider.HabitTrackProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest

class HabitDetailsViewModel(
    habitProvider: HabitProvider,
    habitTrackProvider: HabitTrackProvider,
    habitAbstinenceProvider: HabitAbstinenceProvider,
    habitStatisticsProvider: HabitStatisticsProvider,
    dateTimeProvider: DateTimeProvider,
    habitId: Int
) : ViewModel() {

    val habitController = LoadingController(
        coroutineScope = viewModelScope,
        flow = habitProvider.habitFlow(habitId)
    )

    val habitTracksController = LoadingController(
        coroutineScope = viewModelScope,
        flow = habitTrackProvider.habitTracksFlow(habitId)
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentMonthDailyCountsController = LoadingController(
        coroutineScope = viewModelScope,
        flow = dateTimeProvider.currentDateTimeFlow().flatMapLatest {
            habitTrackProvider.provideDailyEventCountByRange(
                habitId = habitId,
                range = it..it
            )
        }
    )

    val habitAbstinenceController = LoadingController(
        coroutineScope = viewModelScope,
        flow = habitAbstinenceProvider.currentAbstinenceFlow(habitId)
    )

    val abstinenceListController = LoadingController(
        coroutineScope = viewModelScope,
        flow = habitAbstinenceProvider.abstinenceListFlow(habitId)
    )

    val statisticsController = LoadingController(
        coroutineScope = viewModelScope,
        flow = habitStatisticsProvider.statisticsFlow(habitId)
    )
}