package epicarchitect.breakbadhabits.resources.strings.habits.records.editing

import epicarchitect.breakbadhabits.habits.validation.DailyHabitEventCountError
import epicarchitect.breakbadhabits.habits.validation.HabitEventRecordTimeRangeError

interface HabitEventRecordEditingStrings {
    fun dailyEventCountError(error: DailyHabitEventCountError): String
    fun titleText(habitName: String): String
    fun commentDescription(): String
    fun commentTitle(): String
    fun finishDescription(): String
    fun finishButton(): String
    fun deleteConfirmation(): String
    fun yes(): String
    fun cancel(): String
    fun deleteDescription(): String
    fun deleteButton(): String
    fun dailyEventCountDescription(): String
    fun dailyEventCountLabel(): String
    fun timeRangeDescription(): String
    fun timeRangeError(error: HabitEventRecordTimeRangeError): String
    fun timeRangeTitle(): String
    fun startDateTimeLabel(): String
    fun endDateTimeLabel(): String
    fun done(): String
}



