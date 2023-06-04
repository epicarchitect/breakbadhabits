package epicarchitect.breakbadhabits.presentation.habits

import epicarchitect.breakbadhabits.foundation.controller.LoadingController
import epicarchitect.breakbadhabits.foundation.controller.SingleRequestController
import epicarchitect.breakbadhabits.foundation.controller.ValidatedInputController
import epicarchitect.breakbadhabits.foundation.controller.validateAndRequire
import epicarchitect.breakbadhabits.foundation.datetime.ZonedDateTimeRangeOfOne
import epicarchitect.breakbadhabits.foundation.viewmodel.ViewModel
import epicarchitect.breakbadhabits.logic.datetime.provider.DateTimeProvider
import epicarchitect.breakbadhabits.logic.habits.creator.HabitTrackCreator
import epicarchitect.breakbadhabits.logic.habits.provider.HabitProvider
import epicarchitect.breakbadhabits.logic.habits.validator.CorrectHabitTrackEventCount
import epicarchitect.breakbadhabits.logic.habits.validator.CorrectHabitTrackTime
import epicarchitect.breakbadhabits.logic.habits.validator.HabitTrackEventCountValidator
import epicarchitect.breakbadhabits.logic.habits.validator.HabitTrackTimeValidator
import kotlinx.coroutines.flow.combine

class HabitTrackCreationViewModel(
    habitProvider: HabitProvider,
    habitTrackCreator: HabitTrackCreator,
    trackRangeValidator: HabitTrackTimeValidator,
    trackEventCountValidator: HabitTrackEventCountValidator,
    dateTimeProvider: DateTimeProvider,
    habitId: Int
) : ViewModel() {

    val habitController = LoadingController(
        coroutineScope = viewModelScope,
        flow = habitProvider.habitFlow(habitId)
    )

    val eventCountInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = 1,
        validation = trackEventCountValidator::validate
    )

    val timeInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = ZonedDateTimeRangeOfOne(dateTimeProvider.getCurrentDateTime()),
        validation = trackRangeValidator::validate
    )

    val commentInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = "",
        validation = { null }
    )

    val creationController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            habitTrackCreator.createHabitTrack(
                habitId = habitId,
                range = timeInputController.validateAndRequire(),
                eventCount = eventCountInputController.validateAndRequire(),
                comment = commentInputController.state.value.input
            )
        },
        isAllowedFlow = combine(
            eventCountInputController.state,
            timeInputController.state,
        ) { trackValue, trackRange ->
            trackRange.validationResult.let {
                it == null || it is CorrectHabitTrackTime
            } && trackValue.validationResult.let {
                it == null || it is CorrectHabitTrackEventCount
            }
        }
    )
}