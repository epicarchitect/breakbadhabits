package epicarchitect.breakbadhabits.resources.strings.habits.records.editing

import epicarchitect.breakbadhabits.habits.validation.HabitEventCountError
import epicarchitect.breakbadhabits.habits.validation.HabitEventRecordTimeRangeError

class EnglishHabitEventRecordEditingStrings : HabitEventRecordEditingStrings {
    override fun titleText(habitName: String) = "Editing a record — $habitName"
    override fun commentDescription() = "You can write a comment, but you don't have to."
    override fun commentTitle() = "Comment"
    override fun finishDescription() = "You can always change or delete this record."
    override fun finishButton() = "Save changes"
    override fun deleteConfirmation() = "Are you sure you want to delete this record?"
    override fun deleteDescription() = "You can delete this record."
    override fun deleteButton() = "Delete this record"
    override fun yes() = "Yes"
    override fun cancel() = "Cancel"
    override fun eventCountError(error: HabitEventCountError) = when (error) {
        HabitEventCountError.Empty -> {
            "Cant be empty"
        }
    }

    override fun startDateTimeLabel() = "Start"
    override fun endDateTimeLabel() = "End"
    override fun done() = "Done"


    override fun timeRangeTitle() = "Time range"
    override fun timeRangeError(error: HabitEventRecordTimeRangeError) = when (error) {
        HabitEventRecordTimeRangeError.BiggestThenCurrentTime -> "The date and time cannot be greater than the current time."
    }

    override fun eventCountDescription() = "Enter how many habit events there were."
    override fun eventCountTitle() = "Number of events"
    override fun timeRangeDescription() = "Enter the time range in which the habit events occurred."
}