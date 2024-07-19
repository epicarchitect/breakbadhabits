package epicarchitect.breakbadhabits.resources.strings.habits.records.editing

import epicarchitect.breakbadhabits.habits.validation.DailyHabitEventCountError
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
    override fun dailyEventCountError(error: DailyHabitEventCountError) = when (error) {
        DailyHabitEventCountError.Empty -> {
            "Cant be empty"
        }
    }
    override fun timeRangeError(error: HabitEventRecordTimeRangeError) = when (error) {
        HabitEventRecordTimeRangeError.BiggestThenCurrentTime -> "The date and time cannot be greater than the current time."
    }
    override fun timeRangeTitle() = "Time range"
    override fun dailyEventCountTitle() = "Number of events per day"
    override fun dailyEventCountSuffix(count: Int) = "Total: $count"
    override fun dailyEventCountPrefix() = "Events per day: "
    override fun dailyEventCountDescription() = "Indicate approximately how many habit events there were each day."
    override fun totalEventCount(count: Int) = "Total: $count"
    override fun timeRangeDescription() = "Select the time of the first and last habit event."
    override fun startDateTimeLabel() = "First event"
    override fun endDateTimeLabel() = "Last event"
    override fun done() = "Done"
}