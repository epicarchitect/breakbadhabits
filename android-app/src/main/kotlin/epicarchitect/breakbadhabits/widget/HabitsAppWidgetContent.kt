package epicarchitect.breakbadhabits.widget

import android.content.Context
import android.view.View
import android.widget.RemoteViews
import epicarchitect.breakbadhabits.R
import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.database.HabitWidget
import epicarchitect.breakbadhabits.habits.HabitProgress
import epicarchitect.breakbadhabits.habits.HabitStage
import epicarchitect.breakbadhabits.habits.abstinence
import epicarchitect.breakbadhabits.habits.habitProgress
import epicarchitect.breakbadhabits.infrastructure.AppDatabase
import kotlinx.coroutines.flow.first
import kotlin.math.roundToInt
import kotlin.time.Clock
import kotlin.time.Duration

internal data class HabitsAppWidgetItem(
    val habit: Habit,
    val abstinence: Duration?,
    val progress: HabitProgress?
)

internal suspend fun loadHabitsAppWidgetItems(
    widget: HabitWidget
): List<HabitsAppWidgetItem> {
    val database = AppDatabase
    val habits = database.habitsDao.habits().first().associateBy { it.id }
    val currentTime = Clock.System.now()

    return widget.habitIds.mapNotNull { habitId ->
        habits[habitId]?.let { habit ->
            val lastRecord = database.habitEventRecordsDao
                .recordByHabitIdAndMaxEndTime(habitId)
                .first()
            val abstinence = lastRecord?.abstinence(currentTime)

            HabitsAppWidgetItem(
                habit = habit,
                abstinence = abstinence,
                progress = abstinence?.let(::habitProgress)
            )
        }
    }
}

internal fun createHabitsAppWidgetItemView(
    context: Context,
    item: HabitsAppWidgetItem
): RemoteViews = RemoteViews(
    context.packageName,
    R.layout.habits_app_widget_item
).apply {
    setTextViewText(R.id.habitName_textView, item.habit.name)
    setTextViewText(
        R.id.abstinenceTime_textView,
        item.abstinence?.let { formatAbstinence(context, it) }
            ?: context.getString(R.string.widget_has_no_events)
    )
    item.progress?.let { progress ->
        setTextViewText(
            R.id.habitStage_textView,
            context.getString(
                R.string.widget_habit_stage_progress,
                context.getString(progress.titleResource()),
                progress.days
            )
        )
        setViewVisibility(R.id.habitStage_textView, View.VISIBLE)
        setViewVisibility(R.id.habitProgress_linearLayout, View.VISIBLE)
        HabitStage.entries.forEachIndexed { index, stage ->
            val segmentDays = stage.endDay - stage.startDay
            val segmentProgress = when {
                progress.isComplete || stage.ordinal < progress.stage.ordinal -> segmentDays
                stage == progress.stage -> (progress.stageFraction * segmentDays).roundToInt()
                else -> 0
            }
            setProgressBar(
                HabitProgressBarIds[index],
                segmentDays,
                segmentProgress,
                false
            )
        }
    }
}

private val HabitProgressBarIds = intArrayOf(
    R.id.habitProgress1_progressBar,
    R.id.habitProgress2_progressBar,
    R.id.habitProgress3_progressBar,
    R.id.habitProgress4_progressBar,
    R.id.habitProgress5_progressBar
)

private fun HabitProgress.titleResource() =
    if (isComplete) R.string.widget_habit_stage_freedom
    else stage.titleResource()

private fun HabitStage.titleResource() = when (this) {
    HabitStage.ACUTE -> R.string.widget_habit_stage_acute
    HabitStage.WITHDRAWAL -> R.string.widget_habit_stage_withdrawal
    HabitStage.REBUILDING -> R.string.widget_habit_stage_rebuilding
    HabitStage.CONSOLIDATION -> R.string.widget_habit_stage_consolidation
    HabitStage.FINAL_CONSOLIDATION -> R.string.widget_habit_stage_final_consolidation
}

internal fun formatAbstinence(context: Context, duration: Duration): String {
    val days = duration.inWholeDays
    val hours = duration.inWholeHours % 24
    val minutes = duration.inWholeMinutes % 60
    return when {
        days > 0L && hours > 0L -> context.getString(
            R.string.widget_abstinence_days_hours,
            days,
            hours
        )

        days > 0L -> context.getString(R.string.widget_abstinence_days, days)

        hours > 0L && minutes > 0L -> context.getString(
            R.string.widget_abstinence_hours_minutes,
            hours,
            minutes
        )

        hours > 0L -> context.getString(R.string.widget_abstinence_hours, hours)

        minutes > 0L -> context.getString(R.string.widget_abstinence_minutes, minutes)

        else -> context.getString(R.string.widget_abstinence_less_than_minute)
    }
}
