package epicarchitect.breakbadhabits.entity.habits

import epicarchitect.breakbadhabits.entity.datetime.DateTime
import epicarchitect.breakbadhabits.entity.datetime.averageDuration
import epicarchitect.breakbadhabits.entity.datetime.duration
import epicarchitect.breakbadhabits.entity.datetime.maxDuration
import epicarchitect.breakbadhabits.entity.datetime.minDuration
import kotlinx.datetime.Instant

class DefaultHabitAbstinenceStatistics(
    private val history: HabitAbstinenceHistory,
    private val dateTime: DateTime
) : HabitAbstinenceStatistics {
    override fun averageDuration() = history.abstinenceRanges().averageDuration()

    override fun maxDuration() = history.abstinenceRanges().maxDuration()

    override fun minDuration() = history.abstinenceRanges().minDuration()

    override fun durationSinceFirstTrack() = history.failedRanges().minOfOrNull(ClosedRange<Instant>::start)?.let {
        (dateTime.instant()..it).duration()
    }
}