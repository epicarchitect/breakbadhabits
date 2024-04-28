package epicarchitect.breakbadhabits.features.habits.editing

import epicarchitect.breakbadhabits.foundation.icons.Icons
import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase

class HabitEditingDependencies(
    val habitId: Int,
    val navigation: HabitEditingNavigation,
    val mainDatabase: MainDatabase,
    val resources: HabitEditingResources,
    val habitIcons: Icons
)