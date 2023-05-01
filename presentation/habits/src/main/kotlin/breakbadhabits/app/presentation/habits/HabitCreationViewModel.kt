package breakbadhabits.app.presentation.habits

import android.util.Log
import androidx.lifecycle.viewModelScope
import breakbadhabits.app.logic.datetime.provider.DateTimeProvider
import breakbadhabits.app.logic.habits.creator.HabitCreator
import breakbadhabits.app.logic.habits.validator.CorrectHabitNewName
import breakbadhabits.app.logic.habits.validator.CorrectHabitTrackEventCount
import breakbadhabits.app.logic.habits.validator.CorrectHabitTrackTime
import breakbadhabits.app.logic.habits.validator.HabitNewNameValidator
import breakbadhabits.app.logic.habits.validator.HabitTrackEventCountValidator
import breakbadhabits.app.logic.habits.validator.HabitTrackTimeValidator
import breakbadhabits.app.logic.icons.LocalIcon
import breakbadhabits.app.logic.icons.LocalIconProvider
import breakbadhabits.foundation.controller.SingleRequestController
import breakbadhabits.foundation.controller.SingleSelectionController
import breakbadhabits.foundation.controller.ValidatedInputController
import breakbadhabits.foundation.datetime.duration
import breakbadhabits.foundation.math.ranges.asRangeOfOne
import breakbadhabits.foundation.viewmodel.ViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.datetime.periodUntil

class HabitCreationViewModel(
    habitCreator: HabitCreator,
    habitNewNameValidator: HabitNewNameValidator,
    trackTimeValidator: HabitTrackTimeValidator,
    trackEventCountValidator: HabitTrackEventCountValidator,
    dateTimeProvider: DateTimeProvider,
    localIconProvider: LocalIconProvider
) : ViewModel() {

    val habitIconSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        items = localIconProvider.getIcons(),
        default = List<LocalIcon>::first
    )

    val habitNameController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = "",
        validation = habitNewNameValidator::validate
    )

    val dailyEventCountInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = 1,
        validation = trackEventCountValidator::validate,
    )

    val firstTrackTimeInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = dateTimeProvider.currentTime.value.asRangeOfOne(),
        validation = trackTimeValidator::validate
    )

    val creationController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            val habitIcon = habitIconSelectionController.state.value.selectedItem
            val habitName = habitNameController.validateAndAwait()
            require(habitName is CorrectHabitNewName)

            val dailyEventCount = dailyEventCountInputController.validateAndAwait()
            require(dailyEventCount is CorrectHabitTrackEventCount)

            val firstTrackRange = firstTrackTimeInputController.validateAndAwait()
            require(firstTrackRange is CorrectHabitTrackTime)

            habitCreator.createHabit(
                name = habitName,
                icon = habitIcon,
                trackEventCount = dailyEventCount.data * firstTrackRange.let {
                    it.data.duration.inWholeDays.toInt()
                }, // TODO resolve this
                trackTime = firstTrackRange
            )
        },
        isAllowedFlow = combine(
            habitNameController.state,
            dailyEventCountInputController.state,
            firstTrackTimeInputController.state,
        ) { habitName, firstTrackEventCount, firstTrackRange ->
            habitName.validationResult.let {
                it == null || it is CorrectHabitNewName
            } && firstTrackRange.validationResult.let {
                it == null || it is CorrectHabitTrackTime
            } && firstTrackEventCount.validationResult.let {
                it == null || it is CorrectHabitTrackEventCount
            }
        }
    )
}

