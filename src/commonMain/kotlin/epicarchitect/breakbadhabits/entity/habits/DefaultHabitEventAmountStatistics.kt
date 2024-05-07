package epicarchitect.breakbadhabits.entity.habits

import epicarchitect.breakbadhabits.data.HabitTrack
import epicarchitect.breakbadhabits.entity.datetime.DateTime
import epicarchitect.breakbadhabits.entity.datetime.MonthOfYear
import epicarchitect.breakbadhabits.entity.datetime.monthOfYear
import epicarchitect.breakbadhabits.entity.datetime.previous
import kotlinx.datetime.TimeZone

class DefaultHabitEventAmountStatistics(
    private val habitTracks: List<HabitTrack>,
    private val dateTime: DateTime
) : HabitEventAmountStatistics {
    private fun currentMonth() = dateTime.instant().monthOfYear(dateTime.timeZone())

    override fun currentMonthCount() = habitTracks.countEventsInMonth(
        monthOfYear = dateTime.instant().monthOfYear(dateTime.timeZone()),
        timeZone = dateTime.timeZone()
    )

    override fun previousMonthCount() = habitTracks.countEventsInMonth(
        monthOfYear = currentMonth().previous(),
        timeZone = dateTime.timeZone()
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