package breakbadhabits.app.entity

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone

data class HabitTrack(
    val id: Id,
    val habitId: Habit.Id,
    val time: Time,
    val eventCount: EventCount,
    val comment: Comment?
) {
    data class Id(val value: Long)

    sealed interface Time : ClosedRange<Instant> {
        data class Date(val value: Instant) : Time {
            override val endInclusive = value
            override val start = value
        }

        data class Range(val value: ClosedRange<Instant>) : Time, ClosedRange<Instant> by value

        companion object {
            fun of(
                range: ClosedRange<Instant>
            ) = if (range.start == range.endInclusive) {
                Date(range.start)
            } else {
                Range(range)
            }

            fun of(instant: Instant): Time = Date(instant)
        }
    }

    data class EventCount(val dailyCount: Int)

    data class Comment(val value: String)
}