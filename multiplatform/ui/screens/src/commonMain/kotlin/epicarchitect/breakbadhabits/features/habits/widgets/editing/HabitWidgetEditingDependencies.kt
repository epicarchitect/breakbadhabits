package epicarchitect.breakbadhabits.features.habits.widgets.editing

import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase

class HabitWidgetEditingDependencies(
    val widgetId: Int,
    val resources: HabitWidgetEditingResources,
    val navigation: HabitWidgetEditingNavigation,
    val mainDatabase: MainDatabase
)