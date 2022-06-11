package breakbadhabits.android.app.data

data class HabitsAppWidgetData(
    val id: Int,
    val title: String,
    val appWidgetId: Int,
    val habitIds: List<Int>
)