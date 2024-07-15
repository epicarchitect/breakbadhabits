package epicarchitect.breakbadhabits.environment.resources.strings.habits.records.creation

import epicarchitect.breakbadhabits.operation.habits.validation.DailyHabitEventCountError
import epicarchitect.breakbadhabits.operation.habits.validation.HabitEventRecordTimeRangeError

class EnglishHabitEventRecordCreationStrings : HabitEventRecordCreationStrings {
    override fun titleText(habitName: String) = "New record — $habitName"
    override fun commentDescription() = "You can write a comment, but you don't have to."
    override fun commentTitle() = "Comment"
    override fun finishDescription() = "You can always change or delete this record."
    override fun finishButton() = "Done"
    override fun dailyEventCountError(error: DailyHabitEventCountError) = when (error) {
        DailyHabitEventCountError.Empty -> {
            "Cant be empty"
        }
    }

    override fun timeRangeTitle() = "Time range"
    override fun timeRangeError(error: HabitEventRecordTimeRangeError) = when (error) {
        HabitEventRecordTimeRangeError.BiggestThenCurrentTime -> "The date and time cannot be greater than the current time."
    }

    override fun dailyEventCountDescription() = "Indicate approximately how many habit events per day"
    override fun dailyEventCountTitle() = "Frequency"
    override fun timeRangeDescription() = "Select the time of the first and last habit event"
    override fun now() = "Now"
    override fun yesterday() = "Yesterday"
    override fun yourTimeRange() = "Your interval"
    override fun startDateTimeLabel() = "First event"
    override fun endDateTimeLabel() = "Last event"
    override fun done() = "Done"
}