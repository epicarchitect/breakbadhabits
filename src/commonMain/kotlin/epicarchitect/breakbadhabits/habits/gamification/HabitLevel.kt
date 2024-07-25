package epicarchitect.breakbadhabits.habits.gamification

import kotlin.math.roundToInt
import kotlin.time.Duration

data class HabitLevel(
    val value: Int,
    val requiredAbstinence: Duration,
    val price: Long,
    val accumulatedPrice: Long,
    val coinsPerSecond: Long,
    val nextLevel: HabitLevel?
)

fun HabitLevel.progressPercentToNextLevel(habitAbstinence: Duration): Int {
    if (nextLevel == null) return 100
    val maxProgress = nextLevel.requiredAbstinence - requiredAbstinence
    val doneProgress = habitAbstinence - requiredAbstinence
    val progress = ((doneProgress * 100.0) / maxProgress).roundToInt()
    return if (progress > 100) 100 else progress
}