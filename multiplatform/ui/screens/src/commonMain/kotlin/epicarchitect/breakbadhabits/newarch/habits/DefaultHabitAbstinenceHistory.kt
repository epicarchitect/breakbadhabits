package epicarchitect.breakbadhabits.newarch.habits

import epicarchitect.breakbadhabits.foundation.math.ranges.combineIntersections
import epicarchitect.breakbadhabits.newarch.time.AppTime
import epicarchitect.breakbadhabits.sqldelight.main.HabitTrack
import kotlinx.datetime.Instant

class DefaultHabitAbstinenceHistory(
    private val habitTracks: List<HabitTrack>,
    private val appTime: AppTime
) : HabitAbstinenceHistory {
    override fun failedRanges() = habitTracks.map { it.startTime..it.endTime }.combineIntersections()

    override fun abstinenceRanges(): List<ClosedRange<Instant>> {
        val ranges = failedRanges()
        return List(ranges.size) { index ->
            if (index == ranges.lastIndex) {
                ranges[index].endInclusive..appTime.instant()
            } else {
                ranges[index].endInclusive..ranges[index + 1].start
            }
        }
    }
}