package epicarchitect.breakbadhabits.features.habits.tracks.editing

import epicarchitect.breakbadhabits.validator.IncorrectHabitTrackEventCount

interface HabitTrackEditingResources {
    fun habitNameLabel(name: String): String
    fun trackEventCountError(reason: IncorrectHabitTrackEventCount.Reason): String
    fun titleText(): String
    fun commentDescription(): String
    fun commentLabel(): String
    fun finishDescription(): String
    fun finishButton(): String
}