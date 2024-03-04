package epicarchitect.breakbadhabits.presentation.habits

import epicarchitect.breakbadhabits.foundation.controller.DataFlowController
import epicarchitect.breakbadhabits.foundation.controller.InputController
import epicarchitect.breakbadhabits.foundation.controller.SingleRequestController
import epicarchitect.breakbadhabits.foundation.controller.ValidatedInputController
import epicarchitect.breakbadhabits.foundation.controller.validateAndRequire
import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineScopeOwner
import epicarchitect.breakbadhabits.logic.datetime.provider.DateTimeProvider
import epicarchitect.breakbadhabits.logic.datetime.provider.getCurrentDateTime
import epicarchitect.breakbadhabits.logic.habits.deleter.HabitTrackDeleter
import epicarchitect.breakbadhabits.logic.habits.model.HabitTrack
import epicarchitect.breakbadhabits.logic.habits.provider.HabitProvider
import epicarchitect.breakbadhabits.logic.habits.provider.HabitTrackProvider
import epicarchitect.breakbadhabits.logic.habits.updater.HabitTrackUpdater
import epicarchitect.breakbadhabits.logic.habits.validator.CorrectHabitTrackDateTimeRange
import epicarchitect.breakbadhabits.logic.habits.validator.CorrectHabitTrackEventCount
import epicarchitect.breakbadhabits.logic.habits.validator.HabitTrackDateTimeRangeValidator
import epicarchitect.breakbadhabits.logic.habits.validator.HabitTrackEventCountValidator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class HabitTrackUpdatingViewModel(
    override val coroutineScope: CoroutineScope,
    habitProvider: HabitProvider,
    habitTrackProvider: HabitTrackProvider,
    habitTrackUpdater: HabitTrackUpdater,
    habitTrackDeleter: HabitTrackDeleter,
    trackRangeValidator: HabitTrackDateTimeRangeValidator,
    trackEventCountValidator: HabitTrackEventCountValidator,
    dateTimeProvider: DateTimeProvider,
    habitTrackId: Int
) : CoroutineScopeOwner {

    private val initialHabitTrack = MutableStateFlow<HabitTrack?>(null)

    val habitController = DataFlowController(
        flow = initialHabitTrack.filterNotNull().flatMapLatest {
            habitProvider.habitFlow(it.habitId)
        }
    )

    val eventCountInputController = ValidatedInputController(
        initialInput = 1,
        validation = trackEventCountValidator::validate
    )

    val timeInputController = ValidatedInputController(
        initialInput = dateTimeProvider.getCurrentDateTime().let { it..it },
        validation = trackRangeValidator::validate
    )

    val commentInputController = InputController(initialInput = "")

    val updatingController = SingleRequestController(
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
            val isChanged = initialTrack?.dateTimeRange != trackRange.input ||
                initialTrack.eventCount != eventCount.input ||
                initialTrack.comment != comment.input

            isChanged && trackRange.validationResult.let {
                it == null || it is CorrectHabitTrackDateTimeRange
            } && eventCount.validationResult.let {
                it == null || it is CorrectHabitTrackEventCount
            }
        }
    )

    val deletionController = SingleRequestController {
        habitTrackDeleter.deleteHabitTrack(habitTrackId)
    }

    init {
        coroutineScope.launch {
            val habitTrack = habitTrackProvider.habitTrackFlow(habitTrackId).first()!!
            initialHabitTrack.value = habitTrack
            timeInputController.changeInput(habitTrack.dateTimeRange)
            eventCountInputController.changeInput(habitTrack.eventCount)
            commentInputController.changeInput(habitTrack.comment)
        }
    }
}