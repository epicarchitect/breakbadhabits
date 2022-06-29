package breakbadhabits.android.data

import breakbadhabits.data.HabitEvent

data class ActualHabitEvent(
    val id: Id,
    val habitId: HabitId,
    val time: Time,
    val comment: Comment?
) : HabitEvent {
    data class Id(val value: Int) : HabitEvent.Id
    data class HabitId(val value: Int) : HabitEvent.HabitId
    data class Time(val value: Long) : HabitEvent.Time
    data class Comment(val value: String) : HabitEvent.Comment
}

fun HabitEvent.actual() = this as ActualHabitEvent
fun HabitEvent.Id.actual() = (this as ActualHabitEvent.Id).value
fun HabitEvent.HabitId.actual() = (this as ActualHabitEvent.HabitId).value
fun HabitEvent.Time.actual() = (this as ActualHabitEvent.Time).value
fun HabitEvent.Comment?.actual() = (this as? ActualHabitEvent.Comment)?.value