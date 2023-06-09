package epicarchitect.breakbadhabits.presentation.habits

import epicarchitect.breakbadhabits.foundation.controller.LoadingController
import epicarchitect.breakbadhabits.foundation.datetime.ZonedDateTimeRange
import epicarchitect.breakbadhabits.foundation.datetime.monthOfYear
import epicarchitect.breakbadhabits.foundation.datetime.next
import epicarchitect.breakbadhabits.foundation.datetime.previous
import epicarchitect.breakbadhabits.foundation.datetime.toInstantAtEnd
import epicarchitect.breakbadhabits.foundation.datetime.toInstantAtStart
import epicarchitect.breakbadhabits.foundation.viewmodel.ViewModel
import epicarchitect.breakbadhabits.logic.datetime.provider.DateTimeProvider
import epicarchitect.breakbadhabits.logic.habits.provider.HabitAbstinenceProvider
import epicarchitect.breakbadhabits.logic.habits.provider.HabitProvider
import epicarchitect.breakbadhabits.logic.habits.provider.HabitStatisticsProvider
import epicarchitect.breakbadhabits.logic.habits.provider.HabitTrackProvider
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
        flow = dateTimeProvider.currentTimeZoneFlow().flatMapLatest { timeZone ->
            dateTimeProvider.currentDateTimeFlow().map {
                it.dateTime.date.monthOfYear
            }.distinctUntilChanged().flatMapLatest { currentMonth ->
                val previousMonth = currentMonth.previous()
                val nextMonth = currentMonth.next()
                val start = previousMonth.toInstantAtStart(timeZone)
                val end = nextMonth.toInstantAtEnd(timeZone)
                habitTrackProvider.provideDailyEventCountByRange(
                    habitId = habitId,
                    range = ZonedDateTimeRange.of(start, end, timeZone)
                )
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