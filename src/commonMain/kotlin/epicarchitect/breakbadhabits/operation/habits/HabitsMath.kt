package epicarchitect.breakbadhabits.operation.habits

import epicarchitect.breakbadhabits.data.HabitEventRecord
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


fun List<HabitEventRecord>.failedRanges() = map {
    it.startTime..it.endTime
}.combineIntersections()

fun habitAbstinenceRangesByFailedRanges(
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

fun List<HabitEventRecord>.countEventsInMonth(
    monthOfYear: MonthOfYear,
    timeZone: TimeZone
) = filterByMonth(monthOfYear, timeZone).countEvents()

fun List<HabitEventRecord>.countEvents() = fold(0) { total, track ->
    total + track.eventCount
}

fun List<HabitEventRecord>.filterByMonth(
    monthOfYear: MonthOfYear,
    timeZone: TimeZone
) = filter {
    monthOfYear in it.timeRange().monthOfYearRange(timeZone)
}

fun HabitEventRecord.timeRange() = startTime..endTime

fun HabitEventRecord.dailyEventCount(timeZone: TimeZone): Int {
    val days = startTime.daysUntil(endTime, timeZone) + 1
    return (eventCount.toFloat() / days).roundToInt()
}

fun totalHabitEventCountByDaily(
    dailyEventCount: Int,
    timeRange: ClosedRange<Instant>,
    timeZone: TimeZone
): Int {
    val days = timeRange.start.daysUntil(timeRange.endInclusive, timeZone) + 1
    return days * dailyEventCount
}

fun List<HabitEventRecord>.groupByMonth(timeZone: TimeZone): Map<MonthOfYear, Collection<HabitEventRecord>> {
    val map = mutableMapOf<MonthOfYear, MutableSet<HabitEventRecord>>()
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

fun HabitEventRecord.abstinence(currentTime: Instant) = (endTime..currentTime).duration()