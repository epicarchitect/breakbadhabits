package epicarchitect.breakbadhabits.features.habits.tracks.list

import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase

class HabitTracksDependencies(
    val habitId: Int,
    val mainDatabase: MainDatabase,
    val resources: HabitTracksResources,
    val navigation: HabitTracksNavigation
)