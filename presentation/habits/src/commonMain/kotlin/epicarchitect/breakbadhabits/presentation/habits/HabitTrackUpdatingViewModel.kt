package epicarchitect.breakbadhabits.presentation.habits

import epicarchitect.breakbadhabits.foundation.controller.InputController
import epicarchitect.breakbadhabits.foundation.controller.LoadingController
import epicarchitect.breakbadhabits.foundation.controller.SingleRequestController
import epicarchitect.breakbadhabits.foundation.controller.ValidatedInputController
import epicarchitect.breakbadhabits.foundation.controller.validateAndRequire
import epicarchitect.breakbadhabits.foundation.datetime.ZonedDateTimeRangeOfOne
import epicarchitect.breakbadhabits.foundation.viewmodel.ViewModel
import epicarchitect.breakbadhabits.logic.datetime.provider.DateTimeProvider
import epicarchitect.breakbadhabits.logic.habits.deleter.HabitTrackDeleter
import epicarchitect.breakbadhabits.logic.habits.model.HabitTrack
import epicarchitect.breakbadhabits.logic.habits.provider.HabitProvider
import epicarchitect.breakbadhabits.logic.habits.provider.HabitTrackProvider
import epicarchitect.breakbadhabits.logic.habits.updater.HabitTrackUpdater
import epicarchitect.breakbadhabits.logic.habits.validator.CorrectHabitTrackEventCount
import epicarchitect.breakbadhabits.logic.habits.validator.CorrectHabitTrackTime
import epicarchitect.breakbadhabits.logic.habits.validator.HabitTrackEventCountValidator
import epicarchitect.breakbadhabits.logic.habits.validator.HabitTrackTimeValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
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
        initialInput = ZonedDateTimeRangeOfOne(dateTimeProvider.getCurrentDateTime()),
        validation = trackRangeValidator::validate,
    )

    val commentInputController = InputController(
        coroutineScope = viewModelScope,
        initialInput = ""
    )

    val updatingController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            habitTrackUpdater.updateHabitTrack(
                id = habitTrackId,
                time = timeInputController.validateAndRequire(),
                eventCount = eventCountInputController.validateAndRequire(),
                comment = commentInputController.state.value.input
            )
        },
        isAllowedFlow = combine(
            eventCountInputController.state,
            timeInputController.state,
            commentInputController.state,
            initialHabitTrack
        ) { eventCount, trackRange, comment, initialTrack ->
            val isChanged = initialTrack?.dateTimeRange != trackRange.input
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
            val habitTrack = habitTrackProvider.habitTrackFlow(habitTrackId).first()!!
            initialHabitTrack.value = habitTrack
            timeInputController.changeInput(habitTrack.dateTimeRange)
            eventCountInputController.changeInput(habitTrack.eventCount)
            commentInputController.changeInput(habitTrack.comment)
        }
    }
}