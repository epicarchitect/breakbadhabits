package epicarchitect.breakbadhabits.screens.dashboard

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.intl.Locale

val LocalDashboardResources = compositionLocalOf {
    if (Locale.current.language == "ru") {
        RussianDashboardResources()
    } else {
        EnglishDashboardResources()
    }
}

interface DashboardResources {
    fun titleText(): String
    fun newHabitButtonText(): String
    fun emptyHabitsText(): String
    fun habitHasNoEvents(): String
}

class RussianDashboardResources : DashboardResources {
    override fun titleText() = "Сломай плохие привычки"
    override fun newHabitButtonText() = "Создать новую привычку"
    override fun emptyHabitsText() = "У вас нет плохих привычек!.. Или есть?"
    override fun habitHasNoEvents() = "События отсутствуют"
}

class EnglishDashboardResources : DashboardResources {
    override fun titleText() = "Break Bad Habits"
    override fun newHabitButtonText() = "Create new habit"
    override fun emptyHabitsText() = "You have no bad habits!.. Or not?"
    override fun habitHasNoEvents() = "No events"
}