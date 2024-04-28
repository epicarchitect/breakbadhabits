package epicarchitect.breakbadhabits.logic.habits.newarch.habits

import epicarchitect.breakbadhabits.foundation.datetime.MonthOfYear
import epicarchitect.breakbadhabits.foundation.datetime.monthOfYear
import epicarchitect.breakbadhabits.foundation.datetime.previous
import epicarchitect.breakbadhabits.logic.habits.newarch.time.AppTime
import epicarchitect.breakbadhabits.sqldelight.main.HabitTrack
import kotlinx.datetime.TimeZone

class DefaultHabitEventAmountStatistics(
    private val habitTracks: List<HabitTrack>,
    private val appTime: AppTime
) : HabitEventAmountStatistics {
    private fun currentMonth() = appTime.instant().monthOfYear(appTime.timeZone())

    override fun currentMonthCount() = habitTracks.countEventsInMonth(
        monthOfYear = appTime.instant().monthOfYear(appTime.timeZone()),
        timeZone = appTime.timeZone()
    )

    override fun previousMonthCount() = habitTracks.countEventsInMonth(
        monthOfYear = currentMonth().previous(),
        timeZone = appTime.timeZone()
    )

    override fun totalCount() = habitTracks.countEvents()
}

private fun List<HabitTrack>.countEventsInMonth(
    monthOfYear: MonthOfYear,
    timeZone: TimeZone
) = filterByMonth(monthOfYear, timeZone).countEvents()

private fun List<HabitTrack>.countEvents() = fold(0) { total, track ->
    total + track.eventCount
}

private fun List<HabitTrack>.filterByMonth(
    monthOfYear: MonthOfYear,
    timeZone: TimeZone
) = filter {
    monthOfYear in it.let {
        it.startTime.monthOfYear(timeZone)..it.endTime.monthOfYear(timeZone)
    }
}