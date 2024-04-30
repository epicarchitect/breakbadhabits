package epicarchitect.breakbadhabits.features.habits.tracks.creation

import epicarchitect.breakbadhabits.foundation.identification.IdGenerator
import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase

class HabitTrackCreationDependencies(
    val habitId: Int,
    val navigation: HabitTrackCreationNavigation,
    val resources: HabitTrackCreationResources,
    val mainDatabase: MainDatabase,
    val idGenerator: IdGenerator
)