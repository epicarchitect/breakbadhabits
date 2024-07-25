package epicarchitect.breakbadhabits.habits.gamification

import kotlin.math.roundToInt
import kotlin.time.Duration

data class HabitLevel(
    val value: Int,
    val abstinence: Duration,
    val accumulatedAbstinence: Duration, // sorry for what?
    val price: Long,
    val accumulatedPrice: Long,
    val coinsPerSecond: Long,
    val nextLevel: HabitLevel?
)

fun HabitLevel.progressPercentToNextLevel(habitAbstinence: Duration): Int {
    if (nextLevel == null) return 100
    val maxProgress = nextLevel.abstinence - abstinence
    val doneProgress = habitAbstinence - abstinence
    val progress = ((doneProgress * 100.0) / maxProgress).roundToInt()
    return if (progress > 100) 100 else progress
}