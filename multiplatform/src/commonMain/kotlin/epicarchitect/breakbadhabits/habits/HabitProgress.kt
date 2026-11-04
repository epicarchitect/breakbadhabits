package epicarchitect.breakbadhabits.habits

import kotlin.time.Duration

const val HabitGoalDays = 90

enum class HabitStage(
    val startDay: Int,
    val endDay: Int
) {
    ACUTE(0, 7),
    WITHDRAWAL(7, 21),
    REBUILDING(21, 45),
    CONSOLIDATION(45, 69),
    FINAL_CONSOLIDATION(69, 90);

    val firstDay: Int get() = startDay + 1
    val lastDay: Int get() = endDay
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
    val stage = HabitStage.entries.last { days >= it.startDay }
    val stageFraction = ((exactDays - stage.startDay) / (stage.endDay - stage.startDay))
        .coerceIn(0f, 1f)

    return HabitProgress(
        days = days,
        stage = stage,
        stageFraction = stageFraction,
        isComplete = days >= HabitGoalDays
    )
}
