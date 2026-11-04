package epicarchitect.breakbadhabits.habits

import epicarchitect.breakbadhabits.database.HabitEventRecord
import epicarchitect.breakbadhabits.datetime.MonthOfYear
import epicarchitect.breakbadhabits.datetime.duration
import epicarchitect.breakbadhabits.datetime.monthOfYear
import epicarchitect.breakbadhabits.datetime.mountsBetween
import epicarchitect.breakbadhabits.math.ranges.mergedByOverlappingRanges
import kotlinx.datetime.TimeZone
import kotlin.time.Instant

fun List<HabitEventRecord>.failedRanges() = map {
    it.startTime..it.endTime
}.mergedByOverlappingRanges()

fun habitAbstinenceRangesByFailedRanges(
    failedRanges: List<ClosedRange<Instant>>,
    currentTime: Instant
) = List(failedRanges.size) { index ->
    if (index == failedRanges.lastIndex) {
        failedRanges[index].endInclusive..currentTime
    } else {
        failedRanges[index].endInclusive..failedRanges[index + 1].start
    }
}

fun HabitEventRecord.timeRange() = startTime..endTime

fun List<HabitEventRecord>.groupByMonth(timeZone: TimeZone): Map<MonthOfYear, Set<HabitEventRecord>> {
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