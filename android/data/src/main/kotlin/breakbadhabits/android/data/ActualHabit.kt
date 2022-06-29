package breakbadhabits.android.data

import breakbadhabits.data.Habit

data class ActualHabit(
    val id: Id,
    val name: Name,
    val icon: IconId,
) : Habit {
    data class Id(val value: Int) : Habit.Id
    data class Name(val value: String) : Habit.Name
    data class IconId(val value: Int) : Habit.IconId
}

fun Habit.actual() = this as ActualHabit
fun Habit.Id.actual() = (this as ActualHabit.Id).value
fun Habit.Name.actual() = (this as ActualHabit.Name).value
fun Habit.IconId.actual() = (this as ActualHabit.IconId).value