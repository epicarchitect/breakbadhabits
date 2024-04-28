package epicarchitect.breakbadhabits.defaultDependencies.dashboard

import androidx.compose.ui.text.intl.Locale
import epicarchitect.breakbadhabits.features.dashboard.DashboardResources

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

class LocalizedDashboardResources(locale: Locale) : DashboardResources by (
    if (locale.language == "ru") {
        RussianDashboardResources()
    } else {
        EnglishDashboardResources()
    }
)