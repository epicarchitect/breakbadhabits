package epicarchitect.breakbadhabits.data.resources.strings

interface DashboardStrings {
    fun titleText(): String
    fun newHabitButtonText(): String
    fun emptyHabitsText(): String
    fun habitHasNoEvents(): String
}

class RussianDashboardStrings : DashboardStrings {
    override fun titleText() = "Сломай плохие привычки"
    override fun newHabitButtonText() = "Создать новую привычку"
    override fun emptyHabitsText() = "У вас нет плохих привычек!.. Или есть?"
    override fun habitHasNoEvents() = "События отсутствуют"
}

class EnglishDashboardStrings : DashboardStrings {
    override fun titleText() = "Break Bad Habits"
    override fun newHabitButtonText() = "Create new habit"
    override fun emptyHabitsText() = "You have no bad habits!.. Or not?"
    override fun habitHasNoEvents() = "No events"
}