package epicarchitect.breakbadhabits.habits.widget.android

import android.content.Context
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import epicarchitect.breakbadhabits.android.habits.widget.R
import epicarchitect.breakbadhabits.di.holder.AppModuleHolder
import epicarchitect.breakbadhabits.logic.habits.model.Habit
import epicarchitect.breakbadhabits.logic.habits.model.HabitAbstinence
import epicarchitect.breakbadhabits.ui.format.DurationFormatter
import epicarchitect.breakbadhabits.ui.format.android.AndroidDurationFormatter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class HabitsAppWidgetRemoteViewsFactory(
    private val context: Context,
    private val widgetSystemId: Int
) : RemoteViewsService.RemoteViewsFactory {
    private val durationFormatter = AndroidDurationFormatter(
        resources = context.resources,
        defaultAccuracy = DurationFormatter.Accuracy.HOURS
    )

    private fun loadItems() = runBlocking {
        val config =
            AppModuleHolder.logic.habits.habitWidgetProvider.provideFlowBySystemId(widgetSystemId)
                .first()

        if (config == null) {
            emptyList()
        } else {
            AppModuleHolder.logic.habits.habitProvider.habitsFlow().first().filter {
                config.habitIds.contains(it.id)
            }.map {
                Item(
                    habit = it,
                    abstinence = AppModuleHolder.logic.habits.habitAbstinenceProvider
                        .currentAbstinenceFlow(it.id).first()
                )
            }
        }
    }

    private var items = emptyList<Item>()

    override fun onCreate() = Unit

    override fun onDataSetChanged() {
        items = loadItems()
    }

    override fun onDestroy() {
        items = emptyList()
    }

    override fun getCount() = items.size

    override fun getViewAt(position: Int) = RemoteViews(
        context.packageName,
        R.layout.habits_app_widget_item
    ).apply {
        val item = items[position]
        setTextViewText(R.id.habitName_textView, item.habit.name)
        setTextViewText(
            R.id.abstinenceTime_textView,
            if (item.abstinence == null) {
                context.getString(R.string.habitsAppWidget_noAbstinenceTime)
            } else {
                durationFormatter.format(item.abstinence.duration)
            }
        )
    }

    override fun getLoadingView() = null

    override fun getViewTypeCount() = 1

    override fun getItemId(position: Int) = items[position].habit.id.toLong()

    override fun hasStableIds() = true

    data class Item(
        val habit: Habit,
        val abstinence: HabitAbstinence?
    )
}