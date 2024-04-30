package epicarchitect.breakbadhabits.features.habits.tracks.editing

import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase

class HabitTrackEditingDependencies(
    val habitTrackId: Int,
    val resources: HabitTrackEditingResources,
    val navigation: HabitTrackEditingNavigation,
    val mainDatabase: MainDatabase
)