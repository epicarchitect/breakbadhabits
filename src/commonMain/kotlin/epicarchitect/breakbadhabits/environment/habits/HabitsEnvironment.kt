package epicarchitect.breakbadhabits.environment.habits

import epicarchitect.breakbadhabits.environment.datetime.AppDateTime
import kotlinx.coroutines.CoroutineScope

class HabitsEnvironment(
    coroutineScope: CoroutineScope,
    appDateTime: AppDateTime
) {
    val timePulse = HabitsTimePulse(coroutineScope, appDateTime)
    val maxHabitNameLength = 30
}