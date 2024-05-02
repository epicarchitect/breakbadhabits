package epicarchitect.breakbadhabits.entity.habits

import epicarchitect.breakbadhabits.database.HabitTrack
import epicarchitect.breakbadhabits.entity.math.ranges.combineIntersections
import epicarchitect.breakbadhabits.entity.time.AppTime
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