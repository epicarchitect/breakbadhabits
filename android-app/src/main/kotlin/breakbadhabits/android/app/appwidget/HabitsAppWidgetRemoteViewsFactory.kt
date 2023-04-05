package breakbadhabits.android.app.appwidget

import android.content.Context
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import breakbadhabits.android.app.BreakBadHabitsApp
import breakbadhabits.android.app.R
import breakbadhabits.android.app.format.DurationFormatter
import breakbadhabits.app.logic.habits.model.Habit
import breakbadhabits.app.logic.habits.model.HabitAbstinence
import breakbadhabits.foundation.datetime.toDuration
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class HabitsAppWidgetRemoteViewsFactory(
    private val context: Context,
    private val widgetSystemId: Int
) : RemoteViewsService.RemoteViewsFactory {
    private val logicModule = BreakBadHabitsApp.instance.logicModule
    private val habitProvider = logicModule.habitProvider
    private val habitAbstinenceProvider = logicModule.habitAbstinenceProvider
    private val habitAppWidgetProvider = logicModule.habitWidgetProvider
    private val durationFormatter = DurationFormatter(
        resources = context.resources,
        defaultAccuracy = DurationFormatter.Accuracy.HOURS
    )

    private fun loadItems() = runBlocking {
        val config = habitAppWidgetProvider.provideFlowBySystemId(widgetSystemId).first()

        if (config == null) emptyList()
        else habitProvider.habitsFlow().first().filter {
            config.habitIds.contains(it.id)
        }.map {
            Item(
                habit = it,
                abstinence = habitAbstinenceProvider.currentAbstinenceFlow(it.id).first()
            )
        }
    }

    private var items = emptyList<Item>()

    override fun onCreate() {}

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
                durationFormatter.format(item.abstinence.instantRange.toDuration())
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