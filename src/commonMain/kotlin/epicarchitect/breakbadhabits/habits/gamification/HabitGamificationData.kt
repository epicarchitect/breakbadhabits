package epicarchitect.breakbadhabits.habits.gamification

import epicarchitect.breakbadhabits.database.Habit
import kotlin.time.Duration

data class HabitGamificationData(
    val habitLevel: HabitLevel,
    val progressPercentToNextLevel: Int
)

fun habitGamificationData(
    level: HabitLevel,
    abstinence: Duration
): HabitGamificationData {
    val progressToNextLevel = level.progressPercentToNextLevel(abstinence)

    return HabitGamificationData(
        habitLevel = level,
        progressPercentToNextLevel = progressToNextLevel
    )
}