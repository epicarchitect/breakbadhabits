package breakbadhabits.android.app.appwidget

import android.content.Context
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import breakbadhabits.android.app.R
import breakbadhabits.android.app.data.HabitEvent
import breakbadhabits.android.app.formatter.AbstinenceTimeFormatter
import breakbadhabits.android.app.repository.AppWidgetsRepository
import breakbadhabits.android.app.repository.HabitsRepository
import kotlinx.coroutines.runBlocking


class HabitsAppWidgetRemoteViewsFactory(
    private val context: Context,
    private val habitsRepository: HabitsRepository,
    private val appWidgetsRepository: AppWidgetsRepository,
    private val abstinenceTimeFormatter: AbstinenceTimeFormatter,
    private val appWidgetId: Int
) : RemoteViewsService.RemoteViewsFactory {

    private var items = emptyList<Item>()

    override fun onCreate() {}

    override fun onDataSetChanged() = runBlocking {
        val config = appWidgetsRepository.habitsAppWidgetConfigByAppWidgetId(appWidgetId) ?: let {
            items = emptyList()
            return@runBlocking
        }

        items = habitsRepository.habitListByIds(config.habitIds.toList()).map {
            Item(
                it.id,
                it.name,
                it.iconId,
                habitsRepository.lastByTimeHabitEventByHabitId(it.id)
            )
        }
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
        setTextViewText(R.id.habitName_textView, item.habitName)
        setTextViewText(
            R.id.abstinenceTime_textView,
            if (item.lastHabitEvent == null) {
                context.getString(R.string.habitsAppWidget_noAbstinenceTime)
            } else {
                abstinenceTimeFormatter.format(
                    System.currentTimeMillis() - item.lastHabitEvent.time,
                    maxValueCount = 3
                )
            }
        )
    }

    override fun getLoadingView() = null

    override fun getViewTypeCount() = 1

    override fun getItemId(position: Int) = items[position].habitId.toLong()

    override fun hasStableIds() = true

    data class Item(
        val habitId: Int,
        val habitName: String,
        val habitIconId: Int,
        val lastHabitEvent: HabitEvent?
    )
}