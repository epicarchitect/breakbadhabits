package breakbadhabits.app.entity

data class Habit(
    val id: Id,
    val name: Name,
    val iconResource: IconResource
) {
    data class Id(val value: Long)

    data class Name(val value: String)

    data class IconResource(val iconId: Long)
}
