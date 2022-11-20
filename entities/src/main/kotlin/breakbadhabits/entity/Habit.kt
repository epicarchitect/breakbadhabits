package breakbadhabits.entity

data class Habit(
    val id: Id,
    val name: Name,
    val iconResource: IconResource,
    val countability: Countability
) {
    data class Id(val value: Int)
    data class Name(val value: String)
    data class IconResource(val iconId: Int)
    data class Countability(val idCountable: Boolean)
}
