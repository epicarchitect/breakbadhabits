package epicarchitect.breakbadhabits.features.habits.tracks.list

interface HabitTracksNavigation {
    fun openTrackCreation()
    fun openTrackEditing(habitTrackId: Int)
    fun back()
}