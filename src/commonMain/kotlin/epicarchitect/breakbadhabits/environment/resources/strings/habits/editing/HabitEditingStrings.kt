package epicarchitect.breakbadhabits.environment.resources.strings.habits.editing

import epicarchitect.breakbadhabits.operation.habits.validation.HabitNewNameError

interface HabitEditingStrings {
    fun titleText(): String
    fun habitNameDescription(): String
    fun habitNameTitle(): String
    fun habitIconDescription(): String
    fun finishButtonText(): String
    fun habitNameError(error: HabitNewNameError): String
    fun deleteConfirmation(): String
    fun cancel(): String
    fun yes(): String
    fun deleteDescription(): String
    fun deleteButton(): String
}