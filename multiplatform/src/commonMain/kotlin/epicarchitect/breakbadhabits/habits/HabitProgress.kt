package epicarchitect.breakbadhabits.habits

import kotlin.time.Duration

const val HabitGoalDays = 90

enum class HabitStage(
    val days: IntRange
) {
    RESISTANCE(0 until 7),
    ADJUSTMENT(7 until 21),
    REWIRING(21 until 45),
    CONSOLIDATION(45 until 69),
    LIBERATION(69 until 90);
}

data class HabitProgress(
    val days: Int,
    val stage: HabitStage,
    val stageFraction: Float,
    val isComplete: Boolean
)

fun habitProgress(duration: Duration): HabitProgress {
    val days = duration.inWholeDays.coerceIn(0, HabitGoalDays.toLong()).toInt()
    val exactDays = duration.inWholeSeconds.toFloat() / 86_400f
    val stage = HabitStage.entries.last { days >= it.days.first }
    val stageFraction = ((exactDays - stage.days.first) / stage.days.count().toFloat())
        .coerceIn(0f, 1f)

    return HabitProgress(
        days = days,
        stage = stage,
        stageFraction = stageFraction,
        isComplete = days >= HabitGoalDays
    )
}
