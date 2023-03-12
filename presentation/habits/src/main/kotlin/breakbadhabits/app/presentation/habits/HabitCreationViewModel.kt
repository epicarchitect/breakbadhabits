package breakbadhabits.app.presentation.habits

import androidx.lifecycle.viewModelScope
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.logic.habits.creator.HabitCreator
import breakbadhabits.app.logic.habits.provider.HabitIconProvider
import breakbadhabits.app.logic.habits.validator.CorrectHabitNewName
import breakbadhabits.app.logic.habits.validator.CorrectHabitTrackEventCount
import breakbadhabits.app.logic.habits.validator.CorrectHabitTrackRange
import breakbadhabits.app.logic.habits.validator.HabitNewNameValidator
import breakbadhabits.app.logic.habits.validator.HabitTrackEventCountValidator
import breakbadhabits.app.logic.habits.validator.HabitTrackRangeValidator
import breakbadhabits.foundation.controller.RequestController
import breakbadhabits.foundation.controller.SingleSelectionController
import breakbadhabits.foundation.controller.ValidatedInputController
import breakbadhabits.foundation.viewmodel.ViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class HabitCreationViewModel(
    private val habitCreator: HabitCreator,
    private val habitNewNameValidator: HabitNewNameValidator,
    private val trackRangeValidator: HabitTrackRangeValidator,
    private val trackValueValidator: HabitTrackEventCountValidator,
    habitIconProvider: HabitIconProvider
) : ViewModel() {

    val habitIconSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        items = habitIconProvider.provide(),
        default = List<Habit.Icon>::first
    )

    val habitNameController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = Habit.Name(""),
        validation = habitNewNameValidator::validate
    )

    val firstTrackEventCountInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = HabitTrack.EventCount(dailyCount = 1),
        validation = trackValueValidator::validate
    )

    val firstTrackRangeInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = HabitTrack.Range(
            Clock.System.now().toLocalDateTime(
                timeZone = TimeZone.currentSystemDefault()
            ).let {
                it..it
            }
        ),
        validation = trackRangeValidator::validate
    )

    val creationController = RequestController(
        coroutineScope = viewModelScope,
        request = {
            val habitIcon = habitIconSelectionController.state.value.selectedItem
            val habitName = habitNameController.validateAndAwait()
            require(habitName is CorrectHabitNewName)

            val firstTrackEventCount = firstTrackEventCountInputController.validateAndAwait()
            require(firstTrackEventCount is CorrectHabitTrackEventCount)

            val firstTrackRange = firstTrackRangeInputController.validateAndAwait()
            require(firstTrackRange is CorrectHabitTrackRange)

            habitCreator.createHabit(
                habitName,
                habitIcon,
                firstTrackEventCount,
                firstTrackRange
            )
        },
        isAllowedFlow = combine(
            habitNameController.state,
            firstTrackEventCountInputController.state,
            firstTrackRangeInputController.state,
        ) { habitName, firstTrackEventCount, firstTrackRange ->
            habitName.validationResult.let {
                it == null || it is CorrectHabitNewName
            } && firstTrackRange.validationResult.let {
                it == null || it is CorrectHabitTrackRange
            } && firstTrackEventCount.validationResult.let {
                it == null || it is CorrectHabitTrackEventCount
            }
        }
    )
}

