package epicarchitect.breakbadhabits.entity.habits

import epicarchitect.breakbadhabits.data.HabitTrack
import epicarchitect.breakbadhabits.entity.datetime.DateTime
import epicarchitect.breakbadhabits.entity.math.ranges.combineIntersections
import kotlinx.datetime.Instant

class DefaultHabitAbstinenceHistory(
    private val habitTracks: List<HabitTrack>,
    private val dateTime: DateTime
) : HabitAbstinenceHistory {
    override fun failedRanges() = habitTracks.map { it.startTime..it.endTime }.combineIntersections()

    override fun abstinenceRanges(): List<ClosedRange<Instant>> {
        val ranges = failedRanges()
        return List(ranges.size) { index ->
            if (index == ranges.lastIndex) {
                ranges[index].endInclusive..dateTime.instant()
            } else {
                ranges[index].endInclusive..ranges[index + 1].start
            }
        }
    }
}