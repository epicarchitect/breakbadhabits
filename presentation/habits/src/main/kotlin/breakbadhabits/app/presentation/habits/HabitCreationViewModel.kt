package breakbadhabits.app.presentation.habits

import android.util.Log
import androidx.lifecycle.viewModelScope
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.logic.datetime.config.DateTimeConfigProvider
import breakbadhabits.app.logic.datetime.provider.DateTimeProvider
import breakbadhabits.app.logic.habits.creator.HabitCreator
import breakbadhabits.app.logic.habits.provider.HabitIconProvider
import breakbadhabits.app.logic.habits.validator.CorrectHabitNewName
import breakbadhabits.app.logic.habits.validator.CorrectHabitTrackEventCount
import breakbadhabits.app.logic.habits.validator.CorrectHabitTrackTime
import breakbadhabits.app.logic.habits.validator.HabitNewNameValidator
import breakbadhabits.app.logic.habits.validator.HabitTrackEventCountValidator
import breakbadhabits.app.logic.habits.validator.HabitTrackTimeValidator
import breakbadhabits.foundation.controller.RequestController
import breakbadhabits.foundation.controller.SingleSelectionController
import breakbadhabits.foundation.controller.ValidatedInputController
import breakbadhabits.foundation.datetime.toInstantRange
import breakbadhabits.foundation.viewmodel.ViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toLocalDateTime

class HabitCreationViewModel(
    habitCreator: HabitCreator,
    habitNewNameValidator: HabitNewNameValidator,
    trackTimeValidator: HabitTrackTimeValidator,
    trackEventCountValidator: HabitTrackEventCountValidator,
    dateTimeProvider: DateTimeProvider,
    dateTimeConfigProvider: DateTimeConfigProvider,
    habitIconProvider: HabitIconProvider
) : ViewModel() {

    val habitIconSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        items = habitIconProvider.provide(),
        default = List<Habit.Icon>::first
    )

    val habitNameController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = Habit.Name(""),
        validation = habitNewNameValidator::validate
    )

    val firstTrackEventCountInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = HabitTrack.EventCount(dailyCount = 1),
        validation = trackEventCountValidator::validate,
    )

    val firstTrackTimeInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = HabitTrack.Time.of(dateTimeProvider.getCurrentTime()),
        validation = trackTimeValidator::validate,
        decorateInput = {
            val timeZone = dateTimeConfigProvider.getConfig().appTimeZone
            val start = it.start.toLocalDateTime(timeZone)
            val end = it.endInclusive.toLocalDateTime(timeZone)
            val fixedStart = LocalDateTime(start.date, LocalTime(start.hour, 0, 0))
            val fixedEnd = LocalDateTime(end.date, LocalTime(end.hour, 0, 0))
            HabitTrack.Time.of((fixedStart..fixedEnd).toInstantRange(timeZone))
        }
    )

    val creationController = RequestController(
        coroutineScope = viewModelScope,
        request = {
            val habitIcon = habitIconSelectionController.state.value.selectedItem
            val habitName = habitNameController.validateAndAwait()
            require(habitName is CorrectHabitNewName)

            val firstTrackEventCount = firstTrackEventCountInputController.validateAndAwait()
            require(firstTrackEventCount is CorrectHabitTrackEventCount)

            val firstTrackRange = firstTrackTimeInputController.validateAndAwait()
            require(firstTrackRange is CorrectHabitTrackTime)

            habitCreator.createHabit(
                habitName,
                habitIcon,
                firstTrackEventCount,
                firstTrackRange
            )
        },
        isAllowedFlow = combine(
            habitNameController.state,
            firstTrackEventCountInputController.state,
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

