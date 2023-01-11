package breakbadhabits.entity

data class Habit(
    val id: Id,
    val name: Name,
    val iconResource: IconResource,
    val countability: Countability
) {
    @JvmInline
    value class Id(val value: Int)
    @JvmInline
    value class Name(val value: String)
    @JvmInline
    value class IconResource(val iconId: Int)
    @JvmInline
    value class Countability(val idCountable: Boolean)
}
