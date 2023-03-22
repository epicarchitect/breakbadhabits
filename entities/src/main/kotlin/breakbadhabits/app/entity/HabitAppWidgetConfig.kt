package breakbadhabits.app.entity

data class HabitAppWidgetConfig(
    val id: Id,
    val title: Title,
    val appWidgetId: AppWidgetId,
    val habitIds: List<Habit.Id>
) {
    data class Id(val value: Long)

    data class Title(val value: String)

    data class AppWidgetId(val value: Long)
}