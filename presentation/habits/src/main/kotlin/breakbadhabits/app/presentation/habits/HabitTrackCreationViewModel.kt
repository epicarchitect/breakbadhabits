package breakbadhabits.app.presentation.habits

import androidx.lifecycle.viewModelScope
import breakbadhabits.app.logic.habits.entity.Habit
import breakbadhabits.app.logic.habits.entity.HabitTrack
import breakbadhabits.app.logic.datetime.config.DateTimeConfigProvider
import breakbadhabits.app.logic.datetime.DateTimeProvider
import breakbadhabits.app.logic.habits.tracks.HabitTrackCreator
import breakbadhabits.app.logic.habits.HabitProvider
import breakbadhabits.app.logic.habits.tracks.CorrectHabitTrackEventCount
import breakbadhabits.app.logic.habits.tracks.CorrectHabitTrackTime
import breakbadhabits.app.logic.habits.tracks.HabitTrackEventCountValidator
import breakbadhabits.app.logic.habits.tracks.HabitTrackTimeValidator
import breakbadhabits.foundation.controller.LoadingController
import breakbadhabits.foundation.controller.SingleRequestController
import breakbadhabits.foundation.controller.ValidatedInputController
import breakbadhabits.foundation.datetime.toInstantRange
import breakbadhabits.foundation.viewmodel.ViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toLocalDateTime

class HabitTrackCreationViewModel(
    habitProvider: HabitProvider,
    habitTrackCreator: HabitTrackCreator,
    trackRangeValidator: HabitTrackTimeValidator,
    trackEventCountValidator: HabitTrackEventCountValidator,
    dateTimeProvider: DateTimeProvider,
    dateTimeConfigProvider: DateTimeConfigProvider,
    habitId: Habit.Id
) : ViewModel() {

    val habitController = LoadingController(
        coroutineScope = viewModelScope,
        flow = habitProvider.habitFlow(habitId)
    )

    val eventCountInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = HabitTrack.EventCount(dailyCount = 1),
        validation = trackEventCountValidator::validate
    )

    val timeInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = HabitTrack.Time.of(dateTimeProvider.currentTime.value),
        validation = trackRangeValidator::validate,
        decorateInput = {
            val timeZone = dateTimeConfigProvider.getConfig().appTimeZone
            val start = it.start.toLocalDateTime(timeZone)
            val end = it.endInclusive.toLocalDateTime(timeZone)
            val fixedStart = LocalDateTime(start.date, LocalTime(start.hour, 0, 0))
            val fixedEnd = LocalDateTime(end.date, LocalTime(end.hour, 0, 0))
            HabitTrack.Time.of((fixedStart..fixedEnd).toInstantRange(timeZone))
        }
    )

    val commentInputController = ValidatedInputController<HabitTrack.Comment?, Nothing>(
        coroutineScope = viewModelScope,
        initialInput = null,
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