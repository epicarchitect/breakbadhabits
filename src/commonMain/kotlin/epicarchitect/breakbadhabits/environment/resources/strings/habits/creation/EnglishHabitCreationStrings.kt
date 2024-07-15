package epicarchitect.breakbadhabits.environment.resources.strings.habits.creation

import epicarchitect.breakbadhabits.operation.habits.validation.DailyHabitEventCountError
import epicarchitect.breakbadhabits.operation.habits.validation.HabitNewNameError
import epicarchitect.breakbadhabits.ui.screen.habits.creation.HabitDuration

class EnglishHabitCreationStrings : HabitCreationStrings {
    override fun titleText() = "New habit"
    override fun habitNameDescription() = "Enter a name for the habit, such as smoking."
    override fun habitNameTitle() = "Name"
    override fun habitIconTitle() = "Icon"
    override fun habitEventCountTitle() = "Frequency"
    override fun habitDurationTitle() = "Duration"
    override fun habitIconDescription() = "Choose the appropriate icon for the habit."
    override fun finishButtonText() = "Done"
    override fun habitNameError(error: HabitNewNameError) = when (error) {
        HabitNewNameError.AlreadyUsed -> "This name has already been used."
        HabitNewNameError.Empty       -> "The title cannot be empty."
        is HabitNewNameError.TooLong  -> {
            "The name cannot be longer than ${error.maxLength} characters."
        }
    }

    override fun habitDuration(duration: HabitDuration) = when (duration) {
        HabitDuration.MONTH_1 -> "1 month"
        HabitDuration.MONTH_3 -> "3 months"
        HabitDuration.MONTH_6 -> "6 month"
        HabitDuration.YEAR_1  -> "1 year"
        HabitDuration.YEAR_2  -> "2 years"
        HabitDuration.YEAR_3  -> "3 years"
        HabitDuration.YEAR_4  -> "4 years"
        HabitDuration.YEAR_5  -> "5 years"
        HabitDuration.YEAR_6  -> "6 years"
        HabitDuration.YEAR_7  -> "7 years"
        HabitDuration.YEAR_8  -> "8 years"
        HabitDuration.YEAR_9  -> "9 years"
        HabitDuration.YEAR_10 -> "10 years"
    }

    override fun habitDurationDescription() = "Please indicate approximately how long you have had this habit."

    override fun trackEventCountError(reason: DailyHabitEventCountError) = when (reason) {
        DailyHabitEventCountError.Empty -> {
            "Cant be empty"
        }
    }

    override fun trackEventCountDescription() = "Please indicate approximately how many habit events occurred per day."
}