package epicarchitect.breakbadhabits.newarch.habits

import epicarchitect.breakbadhabits.datetime.averageDuration
import epicarchitect.breakbadhabits.datetime.duration
import epicarchitect.breakbadhabits.datetime.maxDuration
import epicarchitect.breakbadhabits.datetime.minDuration
import epicarchitect.breakbadhabits.newarch.time.AppTime
import kotlinx.datetime.Instant

class DefaultHabitAbstinenceStatistics(
    private val history: HabitAbstinenceHistory,
    private val appTime: AppTime
) : HabitAbstinenceStatistics {
    override fun averageDuration() = history.abstinenceRanges().averageDuration()

    override fun maxDuration() = history.abstinenceRanges().maxDuration()

    override fun minDuration() = history.abstinenceRanges().minDuration()

    override fun durationSinceFirstTrack() = history.failedRanges().minOfOrNull(ClosedRange<Instant>::start)?.let {
        (appTime.instant()..it).duration()
    }
}