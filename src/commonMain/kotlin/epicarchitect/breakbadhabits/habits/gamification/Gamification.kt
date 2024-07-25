package epicarchitect.breakbadhabits.habits.gamification

import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

private const val MAGIC_COEFFICIENT = 1.5791829
private const val HABIT_LEVEL_COUNT = 101

object Gamification {
    private val levels = List(HABIT_LEVEL_COUNT) { level ->
        val abstinence = habitLevelAbstinenceDurationPoint(level)
        HabitLevel(
            level = level,
            abstinence = abstinence,
            coinsPerSecond = coinsPerSecond(level),
            price = priceToLevelUp(abstinence, level)
        )
    }

    fun habitLevelByAbstinence(abstinence: Duration): HabitLevel {
        val nextLevel = levels.indexOfFirst { it.abstinence > abstinence }
        if (nextLevel == 0) return levels.first()
        return levels[nextLevel - 1]
    }

    fun habitLevelProgressPercent(abstinence: Duration): Int {
        val nextLevel = levels.indexOfFirst { it.abstinence > abstinence }
        val currentLevel = nextLevel - 1
        val nextLevelTime = levels[nextLevel]
        val currentLevelTime = levels[currentLevel]
        val maxProgress = nextLevelTime.abstinence - currentLevelTime.abstinence
        val doneProgress = abstinence - currentLevelTime.abstinence
        return ((doneProgress * 100.0) / maxProgress).roundToInt()
    }

    private fun habitLevelAbstinenceDurationPoint(level: Int): Duration {
        val millis = 60000 * level * level.toDouble().pow(MAGIC_COEFFICIENT)
        return millis.milliseconds
    }


    private fun coinsPerSecond(level: Int): Long {
        return ((level * level).toDouble().pow(MAGIC_COEFFICIENT) + 1).roundToLong()
    }

    private fun priceToLevelUp(abstinence: Duration, level: Int): Long {
        if (level == 0) return 0
        return abstinence.inWholeSeconds * coinsPerSecond(level - 1)
    }

}

fun habitGamificationData(abstinence: Duration): HabitGamificationData {
    val habitLevel = Gamification.habitLevelByAbstinence(abstinence)
    val habitLevelProgress = Gamification.habitLevelProgressPercent(abstinence)
    val earnedCoins = habitLevel.coinsPerSecond * abstinence.inWholeSeconds

    return HabitGamificationData(
        habitLevel = habitLevel,
        habitLevelProgressPercent = habitLevelProgress,
        earnedCoins = earnedCoins
    )
}

data class HabitGamificationData(
    val habitLevel: HabitLevel,
    val habitLevelProgressPercent: Int,
    val earnedCoins: Long
)

data class HabitLevel(
    val level: Int,
    val abstinence: Duration,
    val price: Long,
    val coinsPerSecond: Long
)
