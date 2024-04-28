package epicarchitect.breakbadhabits.features.habits.details

import epicarchitect.breakbadhabits.foundation.icons.Icons
import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase

class HabitDetailsDependencies(
    val habitId: Int,
    val navigation: HabitDetailsNavigation,
    val resources: HabitDetailsResources,
    val habitIcons: Icons,
    val mainDatabase: MainDatabase
)