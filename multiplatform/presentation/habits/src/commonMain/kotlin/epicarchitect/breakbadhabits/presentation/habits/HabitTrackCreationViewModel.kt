package epicarchitect.breakbadhabits.presentation.habits

import epicarchitect.breakbadhabits.di.declaration.AppModule
import epicarchitect.breakbadhabits.di.declaration.logic.DateTimeLogicModule
import epicarchitect.breakbadhabits.di.declaration.logic.HabitsLogicModule
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
        flow = appModule.logic.habits.habitProvider.habitFlow(habitId)
    )

    val eventCountInputController = ValidatedInputController(
        initialInput = 1,
        validation = appModule.logic.habits.habitTrackEventCountValidator::validate
    )

    val timeInputController = ValidatedInputController(
        initialInput = appModule.logic.dateTime.dateTimeProvider.getCurrentDateTime().let { it..it },
        validation = appModule.logic.habits.habitTrackDateTimeRangeValidator::validate
    )

    val commentInputController = ValidatedInputController(
        initialInput = "",
        validation = { null }
    )

    val creationController = SingleRequestController(
        request = {
            appModule.logic.habits.habitTrackCreator.createHabitTrack(
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