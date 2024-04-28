package epicarchitect.breakbadhabits.features.habits.widgets

import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase

class HabitWidgetsDependencies(
    val resources: HabitWidgetsResources,
    val navigation: HabitWidgetsNavigation,
    val mainDatabase: MainDatabase
)