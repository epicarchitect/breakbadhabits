package epicarchitect.breakbadhabits.habits

import epicarchitect.breakbadhabits.datetime.AppDateTime
import kotlinx.coroutines.CoroutineScope

class HabitsEnvironment(
    coroutineScope: CoroutineScope,
    dateTime: AppDateTime
) {
    val rules = HabitsRules()
    val timePulse = HabitsTimePulse(coroutineScope, dateTime)
    val icons = HabitIcons()
}