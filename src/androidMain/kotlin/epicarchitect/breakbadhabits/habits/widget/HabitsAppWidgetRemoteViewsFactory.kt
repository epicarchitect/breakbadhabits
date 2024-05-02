package epicarchitect.breakbadhabits.habits.widget

import android.content.Context
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import epicarchitect.breakbadhabits.R
import epicarchitect.breakbadhabits.database.AppData
import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.entity.datetime.duration
import epicarchitect.breakbadhabits.entity.time.SystemAppTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration

class HabitsAppWidgetRemoteViewsFactory(
    private val context: Context,
    private val widgetSystemId: Int
) : RemoteViewsService.RemoteViewsFactory {
//    private val durationFormatter = AndroidDurationFormatter(
//        resources = context.resources,
//        defaultAccuracy = DurationFormatter.Accuracy.HOURS
//    )

    private fun loadItems() = runBlocking {
        val config = AppData.database.habitWidgetQueries.selectById(widgetSystemId)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
            .first()

        val appTime = SystemAppTime()

        if (config == null) {
            emptyList()
        } else {
            AppData.database.habitQueries
                .selectAll().asFlow()
                .mapToList(Dispatchers.IO)
                .first()
                .filter {
                    config.habitIds.contains(it.id)
                }.map {
                    val lastTrack = AppData.database.habitTrackQueries
                        .selectByHabitIdAndMaxEndTime(it.id)
                        .asFlow()
                        .mapToOneOrNull(Dispatchers.IO)
                        .first()

                    val abstinence = lastTrack?.let {
                        (it.endTime..appTime.instant()).duration()
                    }

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
            if (item.abstinence == null) {
                context.getString(R.string.habitsAppWidget_noAbstinenceTime)
            } else {
//                durationFormatter.format(item.abstinence)
                item.abstinence.toString()
            }
        )
    }

    override fun getLoadingView() = null

    override fun getViewTypeCount() = 1

    override fun getItemId(position: Int) = items[position].habit.id.toLong()

    override fun hasStableIds() = true

    data class Item(
        val habit: Habit,
        val abstinence: Duration?
    )
}