package breakbadhabits.app.logic.habits.entity

import breakbadhabits.app.logic.icons.LocalIcon

data class Habit(
    val id: Id,
    val name: Name,
    val icon: Icon
) {
    data class Id(val value: Long)

    data class Name(val value: String)

    data class Icon(val value: LocalIcon)
}
