package breakbadhabits.app.presentation.habits

import androidx.lifecycle.viewModelScope
import breakbadhabits.app.logic.datetime.provider.DateTimeProvider
import breakbadhabits.app.logic.habits.provider.HabitAbstinenceProvider
import breakbadhabits.app.logic.habits.provider.HabitProvider
import breakbadhabits.app.logic.habits.provider.HabitStatisticsProvider
import breakbadhabits.app.logic.habits.provider.HabitTrackProvider
import breakbadhabits.foundation.controller.LoadingController
import breakbadhabits.foundation.datetime.monthOfYear
import breakbadhabits.foundation.datetime.next
import breakbadhabits.foundation.datetime.previous
import breakbadhabits.foundation.datetime.toInstantAtEnd
import breakbadhabits.foundation.datetime.toInstantAtStart
import breakbadhabits.foundation.viewmodel.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

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
        flow = dateTimeProvider.timeZone.flatMapLatest { timeZone ->
            dateTimeProvider.currentTime.map {
                it.monthOfYear(timeZone)
            }.distinctUntilChanged().flatMapLatest { currentMonth ->
                val previousMonth = currentMonth.previous()
                val nextMonth = currentMonth.next()
                val start = previousMonth.toInstantAtStart(timeZone)
                val end = nextMonth.toInstantAtEnd(timeZone)
                habitTrackProvider.provideDailyEventCountByRange(habitId, start..end)
            }
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