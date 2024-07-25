package epicarchitect.breakbadhabits.habits.gamification

import kotlin.math.pow
import kotlin.math.roundToLong
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class HabitLevels(
    maxHabitLevel: Int,
    magicCoefficient: Double
) : List<HabitLevel> by generate(maxHabitLevel, magicCoefficient)

private fun generate(
    maxHabitLevel: Int,
    magicCoefficient: Double
): List<HabitLevel> {
    val levels = mutableListOf<HabitLevel>()

    fun create(
        level: Int,
        previousAccumulatedPrice: Long
    ): HabitLevel {
        val abstinence = requiredAbstinence(magicCoefficient, level)
        val coinsPerSecond = coinsPerSecond(magicCoefficient, level)
        val price = price(abstinence, coinsPerSecond)
        val accumulatedPrice = previousAccumulatedPrice + price
        return HabitLevel(
            value = level,
            requiredAbstinence = abstinence,
            coinsPerSecond = coinsPerSecond,
            price = price,
            accumulatedPrice = accumulatedPrice,
            nextLevel = if (level == maxHabitLevel) null else create(
                level = level + 1,
                previousAccumulatedPrice = accumulatedPrice
            )
        ).also { levels.add(0, it) }
    }

    create(
        level = 0,
        previousAccumulatedPrice = 0
    )

    return levels
}

private fun requiredAbstinence(
    magicCoefficient: Double,
    level: Int
): Duration {
    val minutes = level * level.toDouble().pow(magicCoefficient)
    return minutes.minutes.roundToNice()
}

private fun coinsPerSecond(
    magicCoefficient: Double,
    level: Int
): Long {
    return (level * level + 2.0).pow(magicCoefficient).roundToLong().roundToNice()
}

private fun price(
    abstinence: Duration,
    coinsPerSecond: Long
): Long {
    return abstinence.inWholeSeconds * coinsPerSecond
}

private fun Duration.roundToNice() =
    if (this > 1.days) {
        inWholeHours.hours
    } else {
        inWholeMinutes.roundToNice().minutes
    }

private fun Long.roundToNice() = if (this < 10 || this % 10 == 5L) {
    this
} else if (this % 10 > 5) {
    if (this % 10 < 7 || this % 10 < 3) {
        this - this % 5 // round to smallest
    } else {
        this - this % 5 + 5 // round to greater
    }
} else {
    if (this % 10 < 3) {
        this - this % 5 // round to smallest
    } else {
        this - this % 5 + 5 // round to greater
    }
}