package breakbadhabits.app.entity

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone

data class Habit(
    val id: Id,
    val name: Name,
    val icon: Icon,
    val creationTime: CreationTime
) {
    data class Id(val value: Long)

    data class Name(val value: String)

    data class Icon(val iconId: Long)

    data class CreationTime(val time: Instant, val timeZone: TimeZone)
}
