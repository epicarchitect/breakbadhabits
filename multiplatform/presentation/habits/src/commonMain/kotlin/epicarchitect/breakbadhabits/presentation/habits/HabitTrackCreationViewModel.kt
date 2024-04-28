package epicarchitect.breakbadhabits.presentation.habits

import epicarchitect.breakbadhabits.di.declaration.AppModule
import epicarchitect.breakbadhabits.foundation.controller.DataFlowController
import epicarchitect.breakbadhabits.foundation.controller.SingleRequestController
import epicarchitect.breakbadhabits.foundation.controller.ValidatedInputController
import epicarchitect.breakbadhabits.foundation.controller.validateAndRequire
import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineScopeOwner
import epicarchitect.breakbadhabits.logic.datetime.provider.getCurrentDateTime
import epicarchitect.breakbadhabits.logic.habits.validator.CorrectHabitTrackDateTimeRange
import epicarchitect.breakbadhabits.logic.habits.validator.CorrectHabitTrackEventCount
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.combine

class HabitTrackCreationViewModel(
    override val coroutineScope: CoroutineScope,
    appModule: AppModule,
    habitId: Int
) : CoroutineScopeOwner {

    val habitController = DataFlowController(
        flow = appModule.habits.habitProvider.habitFlow(habitId)
    )

    val eventCountInputController = ValidatedInputController(
        initialInput = 1,
        validation = appModule.habits.habitTrackEventCountValidator::validate
    )

    val timeInputController = ValidatedInputController(
        initialInput = appModule.dateTime.dateTimeProvider.getCurrentDateTime().let { it..it },
        validation = appModule.habits.habitTrackDateTimeRangeValidator::validate
    )

    val commentInputController = ValidatedInputController(
        initialInput = "",
        validation = { null }
    )

    val creationController = SingleRequestController(
        request = {
            appModule.habits.habitTrackCreator.createHabitTrack(
                habitId = habitId,
                range = timeInputController.validateAndRequire(),
                eventCount = eventCountInputController.validateAndRequire(),
                comment = commentInputController.state.value.input
            )
        },
        isAllowedFlow = combine(
            eventCountInputController.state,
            timeInputController.state
        ) { trackValue, trackRange ->
            trackRange.validationResult.let {
                it == null || it is CorrectHabitTrackDateTimeRange
            } && trackValue.validationResult.let {
                it == null || it is CorrectHabitTrackEventCount
            }
        }
    )
}