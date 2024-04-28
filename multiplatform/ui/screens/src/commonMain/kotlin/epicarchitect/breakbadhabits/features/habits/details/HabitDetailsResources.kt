package epicarchitect.breakbadhabits.features.habits.details

interface HabitDetailsResources {
    fun habitHasNoEvents(): String
    fun addHabitTrack(): String
    fun abstinenceChartTitle(): String
    fun statisticsTitle(): String
    fun statisticsAverageAbstinenceTime(): String
    fun statisticsMaxAbstinenceTime(): String
    fun statisticsMinAbstinenceTime(): String
    fun statisticsDurationSinceFirstTrack(): String
    fun statisticsCountEventsInCurrentMonth(): String
    fun statisticsCountEventsInPreviousMonth(): String
    fun statisticsTotalCountEvents(): String
}