package breakbadhabits.entity

data class Habit(
    val id: Id,
    val name: Name,
    val iconResource: IconResource,
    val countability: Countability
) {
    @JvmInline
    value class Id(val value: Long)

    @JvmInline
    value class Name(val value: String)

    @JvmInline
    value class IconResource(val iconId: Long)

    @JvmInline
    value class Countability(val isCountable: Boolean)
}
