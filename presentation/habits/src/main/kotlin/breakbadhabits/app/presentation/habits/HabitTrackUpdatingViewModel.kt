package breakbadhabits.app.presentation.habits

import androidx.lifecycle.viewModelScope
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.logic.habits.deleter.HabitTrackDeleter
import breakbadhabits.app.logic.habits.provider.HabitProvider
import breakbadhabits.app.logic.habits.provider.HabitTrackProvider
import breakbadhabits.app.logic.habits.updater.HabitTrackUpdater
import breakbadhabits.app.logic.habits.validator.CorrectHabitTrackEventCount
import breakbadhabits.app.logic.habits.validator.CorrectHabitTrackRange
import breakbadhabits.app.logic.habits.validator.HabitTrackEventCountValidator
import breakbadhabits.app.logic.habits.validator.HabitTrackRangeValidator
import breakbadhabits.foundation.controller.LoadingController
import breakbadhabits.foundation.controller.RequestController
import breakbadhabits.foundation.controller.ValidatedInputController
import breakbadhabits.foundation.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class HabitTrackUpdatingViewModel(
    private val habitProvider: HabitProvider,
    private val habitTrackProvider: HabitTrackProvider,
    private val habitTrackUpdater: HabitTrackUpdater,
    private val habitTrackDeleter: HabitTrackDeleter,
    private val trackRangeValidator: HabitTrackRangeValidator,
    private val trackEventCountValidator: HabitTrackEventCountValidator,
    private val habitTrackId: HabitTrack.Id
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
        initialInput = HabitTrack.EventCount(dailyCount = 1),
        validation = trackEventCountValidator::validate
    )

    val rangeInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = HabitTrack.Range(Clock.System.now()..Clock.System.now()),
        validation = trackRangeValidator::validate
    )

    val commentInputController = ValidatedInputController<HabitTrack.Comment?, Nothing>(
        coroutineScope = viewModelScope,
        initialInput = null,
        validation = { null }
    )

    val updatingController = RequestController(
        coroutineScope = viewModelScope,
        request = {
            val eventCount = eventCountInputController.validateAndAwait()
            require(eventCount is CorrectHabitTrackEventCount)
            val trackRange = rangeInputController.validateAndAwait()
            require(trackRange is CorrectHabitTrackRange)

            val trackComment = commentInputController.state.value.input

            habitTrackUpdater.updateHabitTrack(
                id = habitTrackId,
                range = trackRange,
                eventCount = eventCount,
                comment = trackComment
            )
        },
        isAllowedFlow = combine(
            eventCountInputController.state,
            rangeInputController.state,
            commentInputController.state,
            initialHabitTrack
        ) { eventCount, trackRange, comment, initialTrack ->
            val isChanged = initialTrack?.range != trackRange.input
                    || initialTrack.eventCount != eventCount.input
                    || initialTrack.comment != comment.input

            isChanged && trackRange.validationResult.let {
                it == null || it is CorrectHabitTrackRange
            } && eventCount.validationResult.let {
                it == null || it is CorrectHabitTrackEventCount
            }
        }
    )

    val deletionController = RequestController(
        coroutineScope = viewModelScope,
        request = {
            habitTrackDeleter.deleteHabitTrack(habitTrackId)
        }
    )

    init {
        viewModelScope.launch {
            val habitTrack = checkNotNull(habitTrackProvider.getHabitTrack(habitTrackId))
            initialHabitTrack.value = habitTrack
            rangeInputController.changeInput(habitTrack.range)
            eventCountInputController.changeInput(habitTrack.eventCount)
            commentInputController.changeInput(habitTrack.comment)
        }
    }
}