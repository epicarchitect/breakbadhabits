package breakbadhabits.app.entity

data class Habit(
    val id: Id,
    val name: Name,
    val icon: Icon
) {
    data class Id(val value: Long)

    data class Name(val value: String)

    data class Icon(val iconId: Long)
}
