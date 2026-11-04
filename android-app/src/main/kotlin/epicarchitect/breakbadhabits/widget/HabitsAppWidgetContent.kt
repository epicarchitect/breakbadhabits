package epicarchitect.breakbadhabits.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.RemoteViews
import epicarchitect.breakbadhabits.MainActivity
import epicarchitect.breakbadhabits.R
import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.database.HabitWidget
import epicarchitect.breakbadhabits.database.HabitWithLastEvent
import epicarchitect.breakbadhabits.datetime.duration
import epicarchitect.breakbadhabits.format.Accuracy
import epicarchitect.breakbadhabits.format.formattedText
import epicarchitect.breakbadhabits.habits.HabitProgress
import epicarchitect.breakbadhabits.habits.HabitStage
import epicarchitect.breakbadhabits.habits.habitProgress
import epicarchitect.breakbadhabits.uikit.stageNameText
import kotlin.math.roundToInt
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Instant

internal data class HabitsAppWidgetItem(
    val habit: Habit,
    val abstinence: Duration?,
    val progress: HabitProgress?
)

internal suspend fun updateHabitsAppWidgets(
    context: Context,
    manager: AppWidgetManager,
    widgets: List<HabitWidget>,
    habitsWithLastEvent: List<HabitWithLastEvent>,
    isPremium: Boolean,
    widgetSystemIds: IntArray? = null
) {
    val systemIds = widgetSystemIds ?: manager.getAppWidgetIds(
        ComponentName(context, HabitsAppWidgetProvider::class.java)
    )
    if (systemIds.isEmpty()) return

    val currentTime = Clock.System.now()
    val widgetsBySystemId = widgets.associateBy { it.systemId }

    systemIds.forEach { widgetSystemId ->
        runCatching {
            val views = when {
                isPremium.not() -> createPremiumLockedWidget(context)
                else -> {
                    val widget = widgetsBySystemId[widgetSystemId]
                    if (widget == null) {
                        createNotConfiguredWidget(context)
                    } else {
                        createHabitsWidget(
                            context = context,
                            items = habitsAppWidgetItems(
                                widget = widget,
                                habitsWithLastEvent = habitsWithLastEvent,
                                currentTime = currentTime
                            )
                        )
                    }
                }
            }
            manager.updateAppWidget(widgetSystemId, views)
        }
    }
}

internal fun habitsAppWidgetItems(
    widget: HabitWidget,
    habitsWithLastEvent: List<HabitWithLastEvent>,
    currentTime: Instant
): List<HabitsAppWidgetItem> {
    val selectedHabitIds = widget.habitIds.toSet()
    val habits = habitsWithLastEvent
        .filter { it.habit.id in selectedHabitIds }
        .associateBy { it.habit.id }

    return widget.habitIds.mapNotNull { habitId ->
        val habitWithLastEvent = habits[habitId] ?: return@mapNotNull null
        val abstinence = habitWithLastEvent.lastEventEndTime
            ?.let { (it..currentTime).duration() }

        HabitsAppWidgetItem(
            habit = habitWithLastEvent.habit,
            abstinence = abstinence,
            progress = abstinence?.let(::habitProgress)
        )
    }
}

internal suspend fun createHabitsWidget(
    context: Context,
    items: List<HabitsAppWidgetItem>
): RemoteViews = RemoteViews(
    context.packageName,
    R.layout.habits_app_widget
).apply {
    if (items.isEmpty()) {
        setViewVisibility(R.id.habits_listView, View.GONE)
        setViewVisibility(R.id.empty_textView, View.VISIBLE)
        setTextViewText(
            R.id.empty_textView,
            context.getString(R.string.widget_no_habits)
        )
    } else {
        setViewVisibility(R.id.habits_listView, View.VISIBLE)
        setViewVisibility(R.id.empty_textView, View.GONE)
        setRemoteAdapter(
            R.id.habits_listView,
            buildRemoteCollectionItems(context, items)
        )
        setPendingIntentTemplate(
            R.id.habits_listView,
            mainActivityPendingIntent(
                context = context,
                requestCode = 1,
                mutable = true
            )
        )
    }
    setOnClickPendingIntent(R.id.widget_root, mainActivityPendingIntent(context))
}

internal fun createNotConfiguredWidget(context: Context): RemoteViews =
    RemoteViews(context.packageName, R.layout.habits_app_widget).apply {
        setViewVisibility(R.id.habits_listView, View.GONE)
        setViewVisibility(R.id.empty_textView, View.VISIBLE)
        setTextViewText(
            R.id.empty_textView,
            context.getString(R.string.widget_not_configured)
        )
        setOnClickPendingIntent(R.id.widget_root, mainActivityPendingIntent(context))
    }

internal fun createPremiumLockedWidget(context: Context): RemoteViews =
    RemoteViews(context.packageName, R.layout.habits_app_widget).apply {
        setViewVisibility(R.id.habits_listView, View.GONE)
        setViewVisibility(R.id.empty_textView, View.VISIBLE)
        setTextViewText(
            R.id.empty_textView,
            context.getString(R.string.widget_premium_required)
        )
        setOnClickPendingIntent(
            R.id.widget_root,
            mainActivityPendingIntent(
                context = context,
                requestCode = 2,
                paywallSource = "widget"
            )
        )
    }

internal suspend fun createHabitsAppWidgetItemView(
    context: Context,
    item: HabitsAppWidgetItem,
    showDivider: Boolean
): RemoteViews = RemoteViews(
    context.packageName,
    R.layout.habits_app_widget_item
).apply {
    setTextViewText(R.id.habitName_textView, item.habit.name)
    setTextViewText(
        R.id.abstinenceTime_textView,
        item.abstinence?.let { widgetTimeText(context, it) }
            ?: context.getString(R.string.widget_has_no_events)
    )
    item.progress?.let { progress ->
        setTextViewText(R.id.habitStage_textView, progress.stageNameText())
        setViewVisibility(R.id.habitStage_textView, View.VISIBLE)
        setProgress(context, progress)
    }
    setViewVisibility(
        R.id.divider_view,
        if (showDivider) View.VISIBLE else View.GONE
    )
    setOnClickFillInIntent(
        R.id.root_frameLayout,
        Intent().putExtra(
            MainActivity.EXTRA_HABIT_ID,
            item.habit.id.toString()
        )
    )
}

private suspend fun buildRemoteCollectionItems(
    context: Context,
    items: List<HabitsAppWidgetItem>
): RemoteViews.RemoteCollectionItems {
    val builder = RemoteViews.RemoteCollectionItems.Builder()
        .setHasStableIds(true)
        .setViewTypeCount(1)

    items.forEachIndexed { index, item ->
        builder.addItem(
            item.habit.id.hashCode().toLong(),
            createHabitsAppWidgetItemView(
                context = context,
                item = item,
                showDivider = index < items.lastIndex
            )
        )
    }

    return builder.build()
}

private fun RemoteViews.setProgress(
    context: Context,
    progress: HabitProgress
) {
    setViewVisibility(R.id.habitProgress_linearLayout, View.VISIBLE)
    val inactiveTrack = ColorStateList.valueOf(
        context.getColor(R.color.widget_progress_track)
    )
    val activeTrack = ColorStateList.valueOf(
        context.getColor(R.color.widget_progress_track_active)
    )

    HabitStage.entries.forEachIndexed { index, stage ->
        val segmentDays = stage.endDay - stage.startDay
        val segmentProgress = when {
            progress.isComplete || stage.ordinal < progress.stage.ordinal -> segmentDays
            stage == progress.stage -> (progress.stageFraction * segmentDays).roundToInt()
            else -> 0
        }
        val segmentId = HabitProgressBarIds[index]
        setProgressBar(
            segmentId,
            segmentDays,
            segmentProgress,
            false
        )
        setColorStateList(
            segmentId,
            "setProgressBackgroundTintList",
            if (stage == progress.stage && progress.isComplete.not()) {
                activeTrack
            } else {
                inactiveTrack
            }
        )
    }
}

private suspend fun widgetTimeText(
    context: Context,
    duration: Duration
): CharSequence {
    val text = duration.formattedText(Accuracy.HOURS)
    val firstSpace = text.indexOf(' ')
    val dayEnd = if (firstSpace > 0) firstSpace else text.length

    return SpannableString(text).apply {
        setSpan(
            RelativeSizeSpan(1.8f),
            0,
            dayEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            dayEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        setSpan(
            ForegroundColorSpan(context.getColor(R.color.widget_text_primary)),
            0,
            dayEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
}

private val HabitProgressBarIds = intArrayOf(
    R.id.habitProgress1_progressBar,
    R.id.habitProgress2_progressBar,
    R.id.habitProgress3_progressBar,
    R.id.habitProgress4_progressBar,
    R.id.habitProgress5_progressBar
)
