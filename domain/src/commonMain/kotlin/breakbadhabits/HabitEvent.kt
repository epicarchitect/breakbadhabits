package breakbadhabits

data class HabitEvent(
    val id: Id,
    val habitId: HabitId,
    val time: Time,
    val comment: Comment?
) {
    interface Id
    interface HabitId
    interface Time
    interface Comment
}