package epicarchitect.breakbadhabits.habits.widget

import android.content.Context
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import epicarchitect.breakbadhabits.R
import epicarchitect.breakbadhabits.data.AppData
import epicarchitect.breakbadhabits.data.Habit
import epicarchitect.breakbadhabits.operation.habits.abstinence
import epicarchitect.breakbadhabits.ui.format.DurationFormattingAccuracy
import epicarchitect.breakbadhabits.ui.format.formatted
import kotlinx.coroutines.runBlocking

class HabitsAppWidgetRemoteViewsFactory(
    private val context: Context,
    private val widgetSystemId: Int
) : RemoteViewsService.RemoteViewsFactory {

    private fun loadItems() = runBlocking {
        val config = AppData.database.habitWidgetQueries.widgetById(widgetSystemId).executeAsOneOrNull()

        if (config == null) {
            emptyList()
        } else {
            AppData.database.habitQueries.habits().executeAsList().filter {
                config.habitIds.contains(it.id)
            }.map {
                val lastTrack = AppData.database.habitTrackQueries
                    .trackByHabitIdAndMaxEndTime(it.id)
                    .executeAsOneOrNull()

                val abstinence = lastTrack?.abstinence(AppData.dateTime.currentTimeState.value)
                    ?.formatted(DurationFormattingAccuracy.HOURS)


                Item(
                    habit = it,
                    abstinence = abstinence
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
            item.abstinence ?: context.getString(R.string.habitsAppWidget_noAbstinenceTime)
        )
    }

    override fun getLoadingView() = null

    override fun getViewTypeCount() = 1

    override fun getItemId(position: Int) = items[position].habit.id.toLong()

    override fun hasStableIds() = true

    data class Item(
        val habit: Habit,
        val abstinence: String?
    )
}