package epicarchitect.breakbadhabits.screens.habits.details

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.intl.Locale

val LocalHabitDetailsResources = compositionLocalOf {
    if (Locale.current.language == "ru") {
        RussianHabitDetailsResources()
    } else {
        EnglishHabitDetailsResources()
    }
}

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

class RussianHabitDetailsResources : HabitDetailsResources {
    override fun habitHasNoEvents() = "События отсутствуют"
    override fun addHabitTrack() = "Добавить событие"
    override fun abstinenceChartTitle() = "График воздержания"
    override fun statisticsTitle() = "Статистика"
    override fun statisticsAverageAbstinenceTime() = "Среднее время воздержания"
    override fun statisticsMaxAbstinenceTime() = "Максимальное время воздержания"
    override fun statisticsMinAbstinenceTime() = "Минимальное время воздержания"
    override fun statisticsDurationSinceFirstTrack() = "Времени с первого события"
    override fun statisticsCountEventsInCurrentMonth() = "Количество событий в текущем месяце"
    override fun statisticsCountEventsInPreviousMonth() = "Количество событий в прошлом месяце"
    override fun statisticsTotalCountEvents() = "Всего событий"
}

class EnglishHabitDetailsResources : HabitDetailsResources {
    override fun habitHasNoEvents() = "No events"
    override fun addHabitTrack() = "Add event"
    override fun abstinenceChartTitle() = "Abstinence chart"
    override fun statisticsTitle() = "Statistics"
    override fun statisticsAverageAbstinenceTime() = "Average time of abstinence"
    override fun statisticsMaxAbstinenceTime() = "Maximum abstinence time"
    override fun statisticsMinAbstinenceTime() = "Minimum abstinence time"
    override fun statisticsDurationSinceFirstTrack() = "Time since the first event"
    override fun statisticsCountEventsInCurrentMonth() = "Number of events in the current month"
    override fun statisticsCountEventsInPreviousMonth() = "Number of events in the last month"
    override fun statisticsTotalCountEvents() = "Total events"
}