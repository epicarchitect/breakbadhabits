package epicarchitect.breakbadhabits.features.habits.widgets.creation

import epicarchitect.breakbadhabits.foundation.identification.IdGenerator
import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase

class HabitWidgetCreationDependencies(
    val systemWidgetId: Int,
    val resources: HabitWidgetCreationResources,
    val navigation: HabitWidgetCreationNavigation,
    val mainDatabase: MainDatabase,
    val idGenerator: IdGenerator
)