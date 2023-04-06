package breakbadhabits.app.presentation.habits

import androidx.lifecycle.viewModelScope
import breakbadhabits.app.logic.datetime.provider.DateTimeProvider
import breakbadhabits.app.logic.habits.validator.CorrectHabitTrackEventCount
import breakbadhabits.app.logic.habits.validator.CorrectHabitTrackTime
import breakbadhabits.app.logic.habits.provider.HabitProvider
import breakbadhabits.app.logic.habits.creator.HabitTrackCreator
import breakbadhabits.app.logic.habits.validator.HabitTrackEventCountValidator
import breakbadhabits.app.logic.habits.validator.HabitTrackTimeValidator
import breakbadhabits.foundation.controller.LoadingController
import breakbadhabits.foundation.controller.SingleRequestController
import breakbadhabits.foundation.controller.ValidatedInputController
import breakbadhabits.foundation.math.ranges.asRangeOfOne
import breakbadhabits.foundation.viewmodel.ViewModel
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
        initialInput = dateTimeProvider.currentTime.value.asRangeOfOne(),
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
            val eventCount = eventCountInputController.validateAndAwait()
            require(eventCount is CorrectHabitTrackEventCount)
            val trackRange = timeInputController.validateAndAwait()
            require(trackRange is CorrectHabitTrackTime)

            val trackComment = commentInputController.state.value.input

            habitTrackCreator.createHabitTrack(
                habitId = habitId,
                range = trackRange,
                eventCount = eventCount,
                comment = trackComment
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