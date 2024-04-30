package epicarchitect.breakbadhabits.features.habits.widgets.list

import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase

class HabitWidgetsDependencies(
    val resources: HabitWidgetsResources,
    val navigation: HabitWidgetsNavigation,
    val mainDatabase: MainDatabase
)