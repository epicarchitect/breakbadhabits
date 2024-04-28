package epicarchitect.breakbadhabits.features.habits.creation

import epicarchitect.breakbadhabits.newarch.habits.DefaultHabitCreationOperation
import epicarchitect.breakbadhabits.validator.IncorrectHabitNewName
import epicarchitect.breakbadhabits.validator.IncorrectHabitTrackEventCount

interface HabitCreationResources {
    fun titleText(): String
    fun habitNameDescription(): String
    fun habitNameLabel(): String
    fun habitIconDescription(): String
    fun finishButtonText(): String
    fun habitNameValidationError(reason: IncorrectHabitNewName.Reason): String
    fun habitTime(habitTime: DefaultHabitCreationOperation.HabitTime): String
    fun trackEventCountError(reason: IncorrectHabitTrackEventCount.Reason): String
}