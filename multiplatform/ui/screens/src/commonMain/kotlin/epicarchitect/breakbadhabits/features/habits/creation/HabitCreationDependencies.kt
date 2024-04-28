package epicarchitect.breakbadhabits.features.habits.creation

import epicarchitect.breakbadhabits.foundation.icons.Icons
import epicarchitect.breakbadhabits.foundation.identification.IdGenerator
import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase

class HabitCreationDependencies(
    val navigation: HabitCreationNavigation,
    val mainDatabase: MainDatabase,
    val resources: HabitCreationResources,
    val habitIcons: Icons,
    val idGenerator: IdGenerator
)