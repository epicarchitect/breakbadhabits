package breakbadhabits.app.presentation.habit.creation

import ValidatedInputController
import androidx.lifecycle.viewModelScope
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.logic.habit.creator.CorrectHabitNewNewName
import breakbadhabits.app.logic.habit.creator.CorrectHabitTrackRange
import breakbadhabits.app.logic.habit.creator.HabitCountability
import breakbadhabits.app.logic.habit.creator.HabitCreator
import breakbadhabits.app.logic.habit.creator.HabitNewNameValidator
import breakbadhabits.app.logic.habit.creator.HabitTrackIntervalValidator
import breakbadhabits.app.logic.habit.creator.ValidatedHabitTrackInterval
import breakbadhabits.app.logic.habit.icon.provider.HabitIconProvider
import breakbadhabits.framework.controller.RequestController
import breakbadhabits.framework.controller.SingleSelectionController
import breakbadhabits.framework.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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
        validate = nameValidator::validate,
        coroutineScope = viewModelScope
    )

    val habitCountabilityController = ValidatedInputController<HabitCountability?, Unit>(
        initialInput = null,
        validate = {},
        coroutineScope = viewModelScope
    )

    val firstTrackRangeInputController =
        ValidatedInputController<HabitTrack.Range?, ValidatedHabitTrackInterval>(
            initialInput = null,
            validate = {
                if (it == null) null
                else trackIntervalValidator.validate(it)
            },
            coroutineScope = viewModelScope
        )

    val creationController = RequestController(
        request = {
            val habitIcon = habitIconSelectionController.state.value.selectedItem
            val validatedHabitName = habitNameController.state.value.validationResult
            val validatedHabitCountability = habitCountabilityController.state.value.input
            val validatedFirstTrackRange = firstTrackRangeInputController.state.value.validationResult

            require(validatedHabitName is CorrectHabitNewNewName)
            require(validatedFirstTrackRange is CorrectHabitTrackRange)
            requireNotNull(validatedHabitCountability)

            habitCreator.createHabit(
                validatedHabitName,
                habitIcon,
                validatedHabitCountability,
                validatedFirstTrackRange
            )
        },
        coroutineScope = viewModelScope
    )
}

