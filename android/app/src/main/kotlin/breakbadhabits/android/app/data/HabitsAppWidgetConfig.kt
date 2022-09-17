package breakbadhabits.android.app.data

data class HabitsAppWidgetConfig(
    val id: Int,
    val title: String,
    val appWidgetId: Int,
    val habitIds: Set<Int>
)