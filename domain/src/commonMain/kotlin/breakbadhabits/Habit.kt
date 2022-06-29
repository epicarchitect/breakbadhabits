package breakbadhabits

data class Habit(
    val id: Id,
    val name: Name,
    val iconId: IconId
) {
    interface Id
    interface Name
    interface IconId
}