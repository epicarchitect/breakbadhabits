package epicarchitect.breakbadhabits.newarch.habits

class CachedHabitAbstinenceHistory(
    private val base: HabitAbstinenceHistory
) : HabitAbstinenceHistory {
    private val failedRanges by lazy(base::failedRanges)
    private val abstinenceRanges by lazy(base::abstinenceRanges)
    override fun failedRanges() = failedRanges
    override fun abstinenceRanges() = abstinenceRanges
}