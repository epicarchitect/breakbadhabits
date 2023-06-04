package epicarchitect.breakbadhabits.presentation.habits

import epicarchitect.breakbadhabits.foundation.controller.SingleRequestController
import epicarchitect.breakbadhabits.foundation.controller.SingleSelectionController
import epicarchitect.breakbadhabits.foundation.controller.ValidatedInputController
import epicarchitect.breakbadhabits.foundation.controller.requireSelectedItem
import epicarchitect.breakbadhabits.foundation.controller.validateAndRequire
import epicarchitect.breakbadhabits.foundation.datetime.ZonedDateTimeRangeOfOne
import epicarchitect.breakbadhabits.foundation.datetime.duration
import epicarchitect.breakbadhabits.foundation.icons.IconProvider
import epicarchitect.breakbadhabits.foundation.viewmodel.ViewModel
import epicarchitect.breakbadhabits.logic.datetime.provider.DateTimeProvider
import epicarchitect.breakbadhabits.logic.habits.creator.HabitCreator
import epicarchitect.breakbadhabits.logic.habits.validator.CorrectHabitNewName
import epicarchitect.breakbadhabits.logic.habits.validator.CorrectHabitTrackEventCount
import epicarchitect.breakbadhabits.logic.habits.validator.CorrectHabitTrackTime
import epicarchitect.breakbadhabits.logic.habits.validator.HabitNewNameValidator
import epicarchitect.breakbadhabits.logic.habits.validator.HabitTrackEventCountValidator
import epicarchitect.breakbadhabits.logic.habits.validator.HabitTrackTimeValidator
import kotlinx.coroutines.flow.combine

class HabitCreationViewModel(
    habitCreator: HabitCreator,
    habitNewNameValidator: HabitNewNameValidator,
    trackTimeValidator: HabitTrackTimeValidator,
    trackEventCountValidator: HabitTrackEventCountValidator,
    dateTimeProvider: DateTimeProvider,
    iconProvider: IconProvider
) : ViewModel() {

    val habitIconSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        itemsFlow = iconProvider.iconsFlow()
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
        initialInput = ZonedDateTimeRangeOfOne(dateTimeProvider.getCurrentDateTime()),
        validation = trackTimeValidator::validate
    )

    val creationController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            val dailyEventCount =
                dailyEventCountInputController.validateAndRequire<CorrectHabitTrackEventCount>()
            val firstTrackRange =
                firstTrackTimeInputController.validateAndRequire<CorrectHabitTrackTime>()

            habitCreator.createHabit(
                name = habitNameController.validateAndRequire(),
                icon = habitIconSelectionController.requireSelectedItem(),
                trackEventCount = dailyEventCount.data * firstTrackRange.data.duration.inWholeDays.toInt(), // TODO resolve this
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

