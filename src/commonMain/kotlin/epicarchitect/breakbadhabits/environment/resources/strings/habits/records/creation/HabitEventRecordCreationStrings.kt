package epicarchitect.breakbadhabits.environment.resources.strings.habits.records.creation

import epicarchitect.breakbadhabits.operation.habits.validation.DailyHabitEventCountError
import epicarchitect.breakbadhabits.operation.habits.validation.HabitEventRecordTimeRangeError

interface HabitEventRecordCreationStrings {
    fun titleText(habitName: String): String
    fun commentTitle(): String
    fun commentDescription(): String
    fun finishDescription(): String
    fun finishButton(): String
    fun dailyEventCountTitle(): String
    fun dailyEventCountDescription(): String
    fun dailyEventCountError(error: DailyHabitEventCountError): String
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