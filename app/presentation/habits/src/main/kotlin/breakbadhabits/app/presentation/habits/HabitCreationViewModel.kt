package breakbadhabits.app.presentation.habits

import androidx.lifecycle.viewModelScope
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.logic.habits.validator.CorrectHabitNewName
import breakbadhabits.app.logic.habits.validator.CorrectHabitTrackRange
import breakbadhabits.app.logic.habits.creator.HabitCountability
import breakbadhabits.app.logic.habits.creator.HabitCreator
import breakbadhabits.app.logic.habits.validator.HabitNewNameValidator
import breakbadhabits.app.logic.habits.validator.HabitTrackIntervalValidator
import breakbadhabits.app.logic.habits.validator.ValidatedHabitTrackRange
import breakbadhabits.app.logic.habits.provider.HabitIconProvider
import breakbadhabits.framework.controller.RequestController
import breakbadhabits.framework.controller.SingleSelectionController
import breakbadhabits.framework.controller.ValidatedInputController
import breakbadhabits.framework.viewmodel.ViewModel
import kotlinx.coroutines.flow.combine

class HabitCreationViewModel(
    private val habitCreator: HabitCreator,
    private val nameValidator: HabitNewNameValidator,
    private val trackIntervalValidator: HabitTrackIntervalValidator,
    habitIconProvider: HabitIconProvider
) : ViewModel() {

    val habitIconSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        items = habitIconProvider.provide(),
        default = List<Habit.IconResource>::first
    )

    val habitNameController = ValidatedInputController(
        initialInput = Habit.Name(""),
        validation = nameValidator::validate,
        coroutineScope = viewModelScope
    )

    val habitCountabilityController = ValidatedInputController<HabitCountability?, Unit>(
        initialInput = null,
        validation = {
            // TODO add validation
        },
        coroutineScope = viewModelScope
    )

    val firstTrackRangeInputController =
        ValidatedInputController<HabitTrack.Range?, ValidatedHabitTrackRange>(
            initialInput = null,
            validation = {
                it?.let(trackIntervalValidator::validate)
            },
            coroutineScope = viewModelScope
        )

    val creationController = RequestController(
        request = {
            val habitIcon = habitIconSelectionController.state.value.selectedItem
            val habitName = habitNameController.state.value.validationResult
            val habitCountability = habitCountabilityController.state.value.input
            val firstTrackRange = firstTrackRangeInputController.state.value.validationResult

            require(habitName is CorrectHabitNewName)
            require(firstTrackRange is CorrectHabitTrackRange)
            requireNotNull(habitCountability)

            habitCreator.createHabit(
                habitName,
                habitIcon,
                habitCountability,
                firstTrackRange
            )
        },
        isAllowedFlow = combine(
            habitNameController.state,
            habitCountabilityController.state,
            firstTrackRangeInputController.state,
        ) { name, countability, firstTrackRange ->
            name.validationResult is CorrectHabitNewName
                    && firstTrackRange.validationResult is CorrectHabitTrackRange
                    && countability.input != null
        },
        coroutineScope = viewModelScope
    )
}

