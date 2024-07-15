package epicarchitect.breakbadhabits.environment.resources.strings.habits.creation

import epicarchitect.breakbadhabits.operation.habits.validation.DailyHabitEventCountError
import epicarchitect.breakbadhabits.operation.habits.validation.HabitNewNameError
import epicarchitect.breakbadhabits.ui.screen.habits.creation.HabitDuration

interface HabitCreationStrings {
    fun titleText(): String
    fun habitNameDescription(): String
    fun habitNameTitle(): String
    fun habitIconTitle(): String
    fun habitEventCountTitle(): String
    fun habitDurationTitle(): String
    fun habitIconDescription(): String
    fun finishButtonText(): String
    fun habitNameError(error: HabitNewNameError): String
    fun habitDuration(duration: HabitDuration): String // move to duration formatting?
    fun habitDurationDescription(): String
    fun trackEventCountError(reason: DailyHabitEventCountError): String
    fun trackEventCountDescription(): String
}