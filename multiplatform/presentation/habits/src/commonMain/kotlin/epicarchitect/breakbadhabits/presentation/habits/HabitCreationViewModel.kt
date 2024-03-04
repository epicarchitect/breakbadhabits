package epicarchitect.breakbadhabits.presentation.habits

import epicarchitect.breakbadhabits.foundation.controller.SingleRequestController
import epicarchitect.breakbadhabits.foundation.controller.SingleSelectionController
import epicarchitect.breakbadhabits.foundation.controller.ValidatedInputController
import epicarchitect.breakbadhabits.foundation.controller.requireSelectedItem
import epicarchitect.breakbadhabits.foundation.controller.validateAndRequire
import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineScopeOwner
import epicarchitect.breakbadhabits.foundation.datetime.duration
import epicarchitect.breakbadhabits.foundation.icons.IconProvider
import epicarchitect.breakbadhabits.logic.datetime.provider.DateTimeProvider
import epicarchitect.breakbadhabits.logic.datetime.provider.getCurrentDateTime
import epicarchitect.breakbadhabits.logic.habits.creator.HabitCreator
import epicarchitect.breakbadhabits.logic.habits.validator.CorrectHabitNewName
import epicarchitect.breakbadhabits.logic.habits.validator.CorrectHabitTrackDateTimeRange
import epicarchitect.breakbadhabits.logic.habits.validator.CorrectHabitTrackEventCount
import epicarchitect.breakbadhabits.logic.habits.validator.HabitNewNameValidator
import epicarchitect.breakbadhabits.logic.habits.validator.HabitTrackDateTimeRangeValidator
import epicarchitect.breakbadhabits.logic.habits.validator.HabitTrackEventCountValidator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.combine

class HabitCreationViewModel(
    override val coroutineScope: CoroutineScope,
    habitCreator: HabitCreator,
    habitNewNameValidator: HabitNewNameValidator,
    trackTimeValidator: HabitTrackDateTimeRangeValidator,
    trackEventCountValidator: HabitTrackEventCountValidator,
    dateTimeProvider: DateTimeProvider,
    iconProvider: IconProvider
) : CoroutineScopeOwner {

    val habitIconSelectionController = SingleSelectionController(iconProvider.iconsFlow())

    val habitNameController = ValidatedInputController(
        initialInput = "",
        validation = habitNewNameValidator::validate
    )

    val dailyEventCountInputController = ValidatedInputController(
        initialInput = 1,
        validation = trackEventCountValidator::validate
    )

    val firstTrackTimeInputController = ValidatedInputController(
        initialInput = dateTimeProvider.getCurrentDateTime().let { it..it },
        validation = trackTimeValidator::validate
    )

    val creationController = SingleRequestController(
        request = {
            val dailyEventCount =
                dailyEventCountInputController.validateAndRequire<CorrectHabitTrackEventCount>()
            val trackRange =
                firstTrackTimeInputController.validateAndRequire<CorrectHabitTrackDateTimeRange>()

            val eventCount = dailyEventCount.data * trackRange.data.duration(
                timeZone = dateTimeProvider.getCurrentTimeZone()
            ).inWholeDays.toInt() // TODO this is shit

            habitCreator.createHabit(
                name = habitNameController.validateAndRequire(),
                icon = habitIconSelectionController.requireSelectedItem(),
                trackEventCount = eventCount,
                trackTime = trackRange
            )
        },
        isAllowedFlow = combine(
            habitNameController.state,
            dailyEventCountInputController.state,
            firstTrackTimeInputController.state
        ) { habitName, firstTrackEventCount, firstTrackRange ->
            habitName.validationResult.let {
                it == null || it is CorrectHabitNewName
            } && firstTrackRange.validationResult.let {
                it == null || it is CorrectHabitTrackDateTimeRange
            } && firstTrackEventCount.validationResult.let {
                it == null || it is CorrectHabitTrackEventCount
            }
        }
    )
}