package epicarchitect.breakbadhabits.resources.strings.habits.records.creation

import epicarchitect.breakbadhabits.habits.validation.HabitEventCountError
import epicarchitect.breakbadhabits.habits.validation.HabitEventRecordTimeRangeError

interface HabitEventRecordCreationStrings {
    fun titleText(habitName: String): String
    fun commentTitle(): String
    fun commentDescription(): String
    fun finishDescription(): String
    fun finishButton(): String
    fun eventCountTitle(): String
    fun eventCountDescription(): String
    fun eventCountError(error: HabitEventCountError): String
    fun timeRangeTitle(): String
    fun timeRangeDescription(): String
    fun timeRangeError(error: HabitEventRecordTimeRangeError): String
    fun now(): String
    fun yesterday(): String
    fun yourTimeRange(): String
    fun startDateTimeLabel(): String
    fun endDateTimeLabel(): String
    fun done(): String
}