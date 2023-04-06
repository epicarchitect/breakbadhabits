package breakbadhabits.app.presentation.habits

import androidx.lifecycle.viewModelScope
import breakbadhabits.app.logic.datetime.provider.DateTimeProvider
import breakbadhabits.app.logic.habits.validator.CorrectHabitTrackEventCount
import breakbadhabits.app.logic.habits.validator.CorrectHabitTrackTime
import breakbadhabits.app.logic.habits.provider.HabitProvider
import breakbadhabits.app.logic.habits.deleter.HabitTrackDeleter
import breakbadhabits.app.logic.habits.validator.HabitTrackEventCountValidator
import breakbadhabits.app.logic.habits.provider.HabitTrackProvider
import breakbadhabits.app.logic.habits.validator.HabitTrackTimeValidator
import breakbadhabits.app.logic.habits.updater.HabitTrackUpdater
import breakbadhabits.app.logic.habits.model.HabitTrack
import breakbadhabits.foundation.controller.LoadingController
import breakbadhabits.foundation.controller.SingleRequestController
import breakbadhabits.foundation.controller.ValidatedInputController
import breakbadhabits.foundation.math.ranges.asRangeOfOne
import breakbadhabits.foundation.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class HabitTrackUpdatingViewModel(
    habitProvider: HabitProvider,
    habitTrackProvider: HabitTrackProvider,
    habitTrackUpdater: HabitTrackUpdater,
    habitTrackDeleter: HabitTrackDeleter,
    trackRangeValidator: HabitTrackTimeValidator,
    trackEventCountValidator: HabitTrackEventCountValidator,
    dateTimeProvider: DateTimeProvider,
    habitTrackId: Int
) : ViewModel() {

    private val initialHabitTrack = MutableStateFlow<HabitTrack?>(null)

    val habitController = LoadingController(
        coroutineScope = viewModelScope,
        flow = initialHabitTrack.filterNotNull().flatMapLatest {
            habitProvider.habitFlow(it.habitId)
        }
    )

    val eventCountInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = 1,
        validation = trackEventCountValidator::validate
    )

    val timeInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = dateTimeProvider.currentTime.value.asRangeOfOne(),
        validation = trackRangeValidator::validate,
    )

    val commentInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = "",
        validation = { null }
    )

    val updatingController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            val eventCount = eventCountInputController.validateAndAwait()
            require(eventCount is CorrectHabitTrackEventCount)
            val trackRange = timeInputController.validateAndAwait()
            require(trackRange is CorrectHabitTrackTime)

            val trackComment = commentInputController.state.value.input

            habitTrackUpdater.updateHabitTrack(
                id = habitTrackId,
                time = trackRange,
                eventCount = eventCount,
                comment = trackComment
            )
        },
        isAllowedFlow = combine(
            eventCountInputController.state,
            timeInputController.state,
            commentInputController.state,
            initialHabitTrack
        ) { eventCount, trackRange, comment, initialTrack ->
            val isChanged = initialTrack?.instantRange != trackRange.input
                    || initialTrack.eventCount != eventCount.input
                    || initialTrack.comment != comment.input

            isChanged && trackRange.validationResult.let {
                it == null || it is CorrectHabitTrackTime
            } && eventCount.validationResult.let {
                it == null || it is CorrectHabitTrackEventCount
            }
        }
    )

    val deletionController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            habitTrackDeleter.deleteHabitTrack(habitTrackId)
        }
    )

    init {
        viewModelScope.launch {
            val habitTrack = checkNotNull(habitTrackProvider.getHabitTrack(habitTrackId))
            initialHabitTrack.value = habitTrack
            timeInputController.changeInput(habitTrack.instantRange)
            eventCountInputController.changeInput(habitTrack.eventCount)
            commentInputController.changeInput(habitTrack.comment)
        }
    }
}