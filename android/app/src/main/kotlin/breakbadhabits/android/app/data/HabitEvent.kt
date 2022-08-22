package breakbadhabits.android.app.data

data class HabitEvent(
    val id: Int,
    val habitId: Int,
    val time: Long,
    val comment: String?
)