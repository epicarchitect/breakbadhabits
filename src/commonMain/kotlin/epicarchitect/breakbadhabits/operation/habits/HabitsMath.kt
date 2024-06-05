package epicarchitect.breakbadhabits.operation.habits

import epicarchitect.breakbadhabits.data.HabitTrack
import epicarchitect.breakbadhabits.data.datetime.MonthOfYear
import epicarchitect.breakbadhabits.operation.datetime.duration
import epicarchitect.breakbadhabits.operation.datetime.monthOfYear
import epicarchitect.breakbadhabits.operation.datetime.monthOfYearRange
import epicarchitect.breakbadhabits.operation.datetime.mountsBetween
import epicarchitect.breakbadhabits.operation.math.ranges.combineIntersections
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlin.math.roundToInt


fun List<HabitTrack>.failedRanges() = map {
    it.startTime..it.endTime
}.combineIntersections()

fun abstinenceRangesByFailedRanges(
    failedRanges: List<ClosedRange<Instant>>,
    currentTime: Instant
): List<ClosedRange<Instant>> {
    return List(failedRanges.size) { index ->
        if (index == failedRanges.lastIndex) {
            failedRanges[index].endInclusive..currentTime
        } else {
            failedRanges[index].endInclusive..failedRanges[index + 1].start
        }
    }
}

fun habitAbstinenceDurationSinceFirstTrack(
    failedRanges: List<ClosedRange<Instant>>,
    currentTime: Instant
) = failedRanges.minOfOrNull(ClosedRange<Instant>::start)?.let {
    (it..currentTime).duration()
}

fun List<HabitTrack>.countEventsInMonth(
    monthOfYear: MonthOfYear,
    timeZone: TimeZone
) = filterByMonth(monthOfYear, timeZone).countEvents()

fun List<HabitTrack>.countEvents() = fold(0) { total, track ->
    total + track.eventCount
}

fun List<HabitTrack>.filterByMonth(
    monthOfYear: MonthOfYear,
    timeZone: TimeZone
) = filter {
    monthOfYear in it.timeRange.monthOfYearRange(timeZone)
}

val HabitTrack.timeRange get() = startTime..endTime

fun HabitTrack.dailyEventCount(timeZone: TimeZone): Int {
    val days = startTime.daysUntil(endTime, timeZone) + 1
    return (eventCount.toFloat() / days).roundToInt()
}

fun totalHabitTrackEventCountByDaily(
    dailyEventCount: Int,
    startTime: Instant,
    endTime: Instant,
    timeZone: TimeZone
): Int {
    val days = startTime.daysUntil(endTime, timeZone) + 1
    return days * dailyEventCount
}

fun List<HabitTrack>.groupByMonth(timeZone: TimeZone): Map<MonthOfYear, Collection<HabitTrack>> {
    val map = mutableMapOf<MonthOfYear, MutableSet<HabitTrack>>()
    forEach { track ->
        val startMonth = track.startTime.monthOfYear(timeZone)
        val endMonth = track.endTime.monthOfYear(timeZone)
        val monthRange = startMonth..endMonth
        map.getOrPut(startMonth, ::mutableSetOf).add(track)
        map.getOrPut(endMonth, ::mutableSetOf).add(track)
        monthRange.mountsBetween().forEach {
            map.getOrPut(it, ::mutableSetOf).add(track)
        }
    }
    return map
}

fun HabitTrack.abstinence(currentTime: Instant) = (endTime..currentTime).duration()