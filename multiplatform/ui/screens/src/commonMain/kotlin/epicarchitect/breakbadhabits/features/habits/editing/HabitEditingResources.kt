package epicarchitect.breakbadhabits.features.habits.editing

import epicarchitect.breakbadhabits.validator.IncorrectHabitNewName

interface HabitEditingResources {
    fun titleText(): String
    fun habitNameDescription(): String
    fun habitNameLabel(): String
    fun habitIconDescription(): String
    fun finishButtonText(): String
    fun habitNameValidationError(reason: IncorrectHabitNewName.Reason): String
    fun deleteConfirmation(): String
    fun cancel(): String
    fun yes(): String
    fun deleteDescription(): String
    fun deleteButton(): String
}