package breakbadhabits.android.app.appwidget


//class HabitsAppWidgetRemoteViewsFactory(
//    private val context: Context,
//    private val habitsRepository: DefaultHabitsRepository,
//    private val appWidgetsRepository: AppWidgetsRepository,
//    private val abstinenceTimeFormatter: AbstinenceTimeFormatter,
//    private val appWidgetId: Int
//) : RemoteViewsService.RemoteViewsFactory {
//
//    private var items = emptyList<Item>()
//
//    override fun onCreate() {}
//
//    override fun onDataSetChanged() = runBlocking {
//        val config = appWidgetsRepository.habitsAppWidgetConfigByAppWidgetId(appWidgetId) ?: let {
//            items = emptyList()
//            return@runBlocking
//        }
//
//        items = habitsRepository.habitsByIds(config.habitIds.toList()).map {
//            Item(
//                it.id,
//                it.name,
//                it.iconId,
//                habitsRepository.lastByTimeHabitTrackByHabitId(it.id)
//            )
//        }
//    }
//
//    override fun onDestroy() {
//        items = emptyList()
//    }
//
//    override fun getCount() = items.size
//
//    override fun getViewAt(position: Int) = RemoteViews(
//        context.packageName,
//        R.layout.habits_app_widget_item
//    ).apply {
//        val item = items[position]
//        setTextViewText(R.id.habitName_textView, item.habitName)
//        setTextViewText(
//            R.id.abstinenceTime_textView,
//            if (item.lastHabitEvent == null) {
//                context.getString(R.string.habitsAppWidget_noAbstinenceTime)
//            } else {
//                abstinenceTimeFormatter.format(
//                    System.currentTimeMillis() - item.lastHabitEvent.time,
//                    maxValueCount = 3
//                )
//            }
//        )
//    }
//
//    override fun getLoadingView() = null
//
//    override fun getViewTypeCount() = 1
//
//    override fun getItemId(position: Int) = items[position].habitId.toLong()
//
//    override fun hasStableIds() = true
//
//    data class Item(
//        val habitId: Int,
//        val habitName: String,
//        val habitIconId: Int,
//        val lastHabitEvent: HabitEvent?
//    )
//}