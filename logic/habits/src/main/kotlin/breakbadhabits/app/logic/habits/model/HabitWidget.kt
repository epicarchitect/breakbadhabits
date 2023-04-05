package breakbadhabits.app.logic.habits.model

data class HabitWidget(
    val id: Int,
    val systemId: Int,
    val title: String,
    val habitIds: List<Int>
)