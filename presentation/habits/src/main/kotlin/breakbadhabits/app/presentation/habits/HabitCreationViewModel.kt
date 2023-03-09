package breakbadhabits.app.presentation.habits

import androidx.lifecycle.viewModelScope
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.logic.habits.creator.HabitCreator
import breakbadhabits.app.logic.habits.provider.HabitIconProvider
import breakbadhabits.app.logic.habits.validator.CorrectHabitNewName
import breakbadhabits.app.logic.habits.validator.CorrectHabitTrackRange
import breakbadhabits.app.logic.habits.validator.CorrectHabitTrackValue
import breakbadhabits.app.logic.habits.validator.HabitNewNameValidator
import breakbadhabits.app.logic.habits.validator.HabitTrackIntervalValidator
import breakbadhabits.app.logic.habits.validator.HabitTrackValueValidator
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
    private val habitNameValidator: HabitNewNameValidator,
    private val trackIntervalValidator: HabitTrackIntervalValidator,
    private val trackValueValidator: HabitTrackValueValidator,
    habitIconProvider: HabitIconProvider
) : ViewModel() {

    val habitIconSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        items = habitIconProvider.provide(),
        default = List<Habit.IconResource>::first
    )

    val habitNameController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = Habit.Name(""),
        validation = habitNameValidator::validate
    )

    val firstTrackValueInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = HabitTrack.EventCount(
            value = 0,
            timeUnit = HabitTrack.EventCount.TimeUnit.DAYS
        ),
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
        validation = trackIntervalValidator::validate
    )

    val creationController = RequestController(
        coroutineScope = viewModelScope,
        request = {
            val habitIcon = habitIconSelectionController.state.value.selectedItem
            val habitName = habitNameController.validateAndAwait()
            require(habitName is CorrectHabitNewName)

            val firstTrackValue = firstTrackValueInputController.validateAndAwait()
            require(firstTrackValue is CorrectHabitTrackValue)

            val firstTrackRange = firstTrackRangeInputController.validateAndAwait()
            require(firstTrackRange is CorrectHabitTrackRange)

            habitCreator.createHabit(
                habitName,
                habitIcon,
                firstTrackValue,
                firstTrackRange
            )
        },
        isAllowedFlow = combine(
            habitNameController.state,
            firstTrackValueInputController.state,
            firstTrackRangeInputController.state,
        ) { name, firstTrackValue, firstTrackRange ->
            name.validationResult.let {
                it == null || it is CorrectHabitNewName
            } && firstTrackRange.validationResult.let {
                it == null || it is CorrectHabitTrackRange
            } && firstTrackValue.validationResult.let {
                it == null || it is CorrectHabitTrackValue
            }
        }
    )
}

