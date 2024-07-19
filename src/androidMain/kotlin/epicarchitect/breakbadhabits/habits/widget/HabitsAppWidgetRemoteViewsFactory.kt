package epicarchitect.breakbadhabits.habits.widget

import android.content.Context
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import epicarchitect.breakbadhabits.R
import epicarchitect.breakbadhabits.environment.Environment
import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.operation.habits.abstinence
import epicarchitect.breakbadhabits.ui.format.DurationFormattingAccuracy
import epicarchitect.breakbadhabits.ui.format.formatted
import kotlin.time.Duration

class HabitsAppWidgetRemoteViewsFactory(
    private val context: Context,
    private val widgetSystemId: Int
) : RemoteViewsService.RemoteViewsFactory {

    private fun loadItems(): List<Item> {
        val config = Environment.database.habitWidgetQueries.widgetBySystemId(widgetSystemId)
            .executeAsOneOrNull()

        return if (config == null) {
            emptyList()
        } else {
            val currentTime = Environment.dateTime.currentInstant()
            Environment.database.habitQueries.habits().executeAsList().mapNotNull {
                if (config.habitIds.contains(it.id)) {
                    Item(
                        habit = it,
                        abstinence = Environment.database.habitEventRecordQueries
                            .recordByHabitIdAndMaxEndTime(it.id)
                            .executeAsOneOrNull()
                            ?.abstinence(currentTime)
                    )
                } else {
                    null
                }
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
            item.abstinence?.formatted(
                accuracy = DurationFormattingAccuracy.HOURS
            ) ?: Environment.resources.strings.appDashboardStrings.habitHasNoEvents()
        )
    }

    override fun getLoadingView() = null

    override fun getViewTypeCount() = 1

    override fun getItemId(position: Int) = items[position].habit.id.toLong()

    override fun hasStableIds() = true

    private data class Item(
        val habit: Habit,
        val abstinence: Duration?
    )
}