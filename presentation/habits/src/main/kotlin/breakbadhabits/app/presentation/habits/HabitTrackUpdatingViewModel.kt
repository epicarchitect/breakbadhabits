package breakbadhabits.app.presentation.habits

import androidx.lifecycle.viewModelScope
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.logic.datetime.config.DateTimeConfigProvider
import breakbadhabits.app.logic.habits.tracks.HabitTrackDeleter
import breakbadhabits.app.logic.habits.HabitProvider
import breakbadhabits.app.logic.habits.tracks.HabitTrackProvider
import breakbadhabits.app.logic.habits.tracks.HabitTrackUpdater
import breakbadhabits.app.logic.habits.tracks.CorrectHabitTrackEventCount
import breakbadhabits.app.logic.habits.tracks.CorrectHabitTrackTime
import breakbadhabits.app.logic.habits.tracks.HabitTrackEventCountValidator
import breakbadhabits.app.logic.habits.tracks.HabitTrackTimeValidator
import breakbadhabits.foundation.controller.LoadingController
import breakbadhabits.foundation.controller.RequestController
import breakbadhabits.foundation.controller.ValidatedInputController
import breakbadhabits.foundation.datetime.toInstantRange
import breakbadhabits.foundation.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toLocalDateTime

class HabitTrackUpdatingViewModel(
    habitProvider: HabitProvider,
    habitTrackProvider: HabitTrackProvider,
    habitTrackUpdater: HabitTrackUpdater,
    habitTrackDeleter: HabitTrackDeleter,
    trackRangeValidator: HabitTrackTimeValidator,
    trackEventCountValidator: HabitTrackEventCountValidator,
    dateTimeConfigProvider: DateTimeConfigProvider,
    habitTrackId: HabitTrack.Id
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

    val timeInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = HabitTrack.Time.of(Clock.System.now()),
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

    val updatingController = RequestController(
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
            val isChanged = initialTrack?.time != trackRange.input
                    || initialTrack.eventCount != eventCount.input
                    || initialTrack.comment != comment.input

            isChanged && trackRange.validationResult.let {
                it == null || it is CorrectHabitTrackTime
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
            timeInputController.changeInput(habitTrack.time)
            eventCountInputController.changeInput(habitTrack.eventCount)
            commentInputController.changeInput(habitTrack.comment)
        }
    }
}