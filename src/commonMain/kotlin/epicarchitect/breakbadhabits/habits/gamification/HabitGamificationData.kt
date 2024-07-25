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
    habitLevel: HabitLevel,
    abstinence: Duration
): HabitGamificationData {
    val progressToNextLevel = habitLevel.progressPercentToNextLevel(abstinence)
    val timeInLevel = abstinence - habit.abstinenceWhenLevelUpgraded
    val earnedCoins = habit.earnedCoinsFromPreviousLevel +
            habitLevel.coinsPerSecond *
            timeInLevel.inWholeSeconds -
            habitLevel.accumulatedPrice


    val upgradeAvailable = progressToNextLevel == 100 &&
            habitLevel.nextLevel != null &&
            habitLevel.nextLevel.price <= earnedCoins

    return HabitGamificationData(
        habitLevel = habitLevel,
        progressPercentToNextLevel = progressToNextLevel,
        earnedCoins = earnedCoins,
        upgradeAvailable = upgradeAvailable
    )
}
