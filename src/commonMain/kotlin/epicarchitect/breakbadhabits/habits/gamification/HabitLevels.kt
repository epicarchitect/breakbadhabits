package epicarchitect.breakbadhabits.habits.gamification

import kotlin.math.pow
import kotlin.math.roundToLong
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class HabitLevels(
    private val maxHabitLevel: Int,
    private val magicCoefficient: Double
) {
    private val levels = generateLevels()
    fun get(value: Int) = levels[value]

    private fun generateLevels(): List<HabitLevel> {
        val levels = mutableListOf<HabitLevel>()

        fun create(
            level: Int,
            prevAccumulatedPrice: Long,
            prevAccumulatedAbstinence: Duration
        ): HabitLevel {
            val abstinence = habitLevelAbstinence(level)
            val price = priceToLevelUp(abstinence, level)
            val accumulatedPrice = prevAccumulatedPrice + price
            val accumulatedAbstinence = prevAccumulatedAbstinence + abstinence
            return HabitLevel(
                value = level,
                abstinence = abstinence,
                accumulatedAbstinence = accumulatedAbstinence,
                coinsPerSecond = coinsPerSecond(level),
                price = price,
                accumulatedPrice = accumulatedPrice,
                nextLevel = if (level == maxHabitLevel) null else create(
                    level = level + 1,
                    prevAccumulatedPrice = accumulatedPrice,
                    prevAccumulatedAbstinence = accumulatedAbstinence
                )
            ).also { levels.add(0, it) }
        }

        create(
            level = 0,
            prevAccumulatedPrice = 0,
            prevAccumulatedAbstinence = Duration.ZERO
        )

        return levels
    }

    private fun habitLevelAbstinence(level: Int): Duration {
        val millis = 60000 * level * level.toDouble().pow(magicCoefficient)
        return millis.milliseconds
    }

    private fun coinsPerSecond(level: Int): Long {
        return ((level * level).toDouble().pow(magicCoefficient) + 1).roundToLong()
    }

    private fun priceToLevelUp(abstinence: Duration, level: Int): Long {
        if (level == 0) return 0
        return abstinence.inWholeSeconds * coinsPerSecond(level - 1)
    }
}