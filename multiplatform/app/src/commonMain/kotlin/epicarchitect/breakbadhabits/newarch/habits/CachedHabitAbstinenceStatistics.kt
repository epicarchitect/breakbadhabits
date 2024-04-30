package epicarchitect.breakbadhabits.newarch.habits

class CachedHabitAbstinenceStatistics(
    private val base: HabitAbstinenceStatistics
) : HabitAbstinenceStatistics {
    private val averageDuration by lazy(base::averageDuration)
    private val maxDuration by lazy(base::maxDuration)
    private val minDuration by lazy(base::minDuration)
    private val durationSinceFirstTrack by lazy(base::durationSinceFirstTrack)
    override fun averageDuration() = averageDuration
    override fun maxDuration() = maxDuration
    override fun minDuration() = minDuration
    override fun durationSinceFirstTrack() = durationSinceFirstTrack
}