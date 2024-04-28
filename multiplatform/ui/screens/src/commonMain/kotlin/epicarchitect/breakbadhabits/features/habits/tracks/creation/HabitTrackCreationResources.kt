package epicarchitect.breakbadhabits.features.habits.tracks.creation

interface HabitTrackCreationResources {
    fun titleText(): String
    fun commentDescription(): String
    fun commentLabel(): String
    fun finishDescription(): String
    fun finishButton(): String
    fun habitNameLabel(habitName: String): String
}