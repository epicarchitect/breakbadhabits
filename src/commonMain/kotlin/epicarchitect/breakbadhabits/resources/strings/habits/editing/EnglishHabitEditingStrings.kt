package epicarchitect.breakbadhabits.resources.strings.habits.editing

import epicarchitect.breakbadhabits.habits.validation.HabitNewNameError

class EnglishHabitEditingStrings : HabitEditingStrings {
    override fun titleText() = "Editing a habit"
    override fun habitNameDescription() = "Enter a name for the habit, such as smoking."
    override fun habitNameTitle() = "Habit name"
    override fun habitIconTitle() = "Icon"
    override fun habitIconDescription() = "Choose the appropriate icon for the habit."
    override fun finishButtonText() = "Save changes"
    override fun habitNameError(error: HabitNewNameError) = when (error) {
        HabitNewNameError.AlreadyUsed -> "This name has already been used."
        HabitNewNameError.Empty -> "The title cannot be empty."
        is HabitNewNameError.TooLong -> {
            "The name cannot be longer than ${error.maxLength} characters."
        }
    }

    override fun deleteConfirmation() = "Are you sure you want to remove this habit?"
    override fun cancel() = "Cancel"
    override fun yes() = "Yes"
    override fun deleteDescription() = "You can delete this habit."
    override fun deleteButton() = "Delete this habit"
}