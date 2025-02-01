package epicarchitect.breakbadhabits.habits.gamification

import epicarchitect.breakbadhabits.database.Habit
import kotlin.time.Duration

data class HabitGamificationData(
    val habitLevel: HabitLevel,
    val progressPercentToNextLevel: Int,
    val earnedCoins: Long,
    val upgradeAvailable: Boolean
)

fun habitGamificationData(
    habit: Habit,
    level: HabitLevel,
    abstinence: Duration
): HabitGamificationData {
    val progressToNextLevel = level.progressPercentToNextLevel(abstinence)
    val abstinenceInLevel = abstinence - habit.abstinenceWhenLevelUpgraded
    val earnedCoins =
        habit.earnedCoinsFromPreviousLevel + level.coinsPerSecond * abstinenceInLevel.inWholeSeconds

    val upgradeAvailable = progressToNextLevel == 100 &&
            level.nextLevel != null &&
            level.nextLevel.price <= earnedCoins

    return HabitGamificationData(
        habitLevel = level,
        progressPercentToNextLevel = progressToNextLevel,
        earnedCoins = earnedCoins,
        upgradeAvailable = upgradeAvailable
    )
}
