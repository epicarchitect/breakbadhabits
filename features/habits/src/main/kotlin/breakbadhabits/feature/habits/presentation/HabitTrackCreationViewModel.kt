package breakbadhabits.feature.habits.presentation

import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitTrack
import breakbadhabits.feature.habits.model.HabitTracksRepository
import breakbadhabits.feature.habits.validator.HabitTrackIntervalValidator
import kolmachikhin.alexander.validation.Correct
import kolmachikhin.alexander.validation.Validated
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HabitTrackCreationViewModel internal constructor(
    private val coroutineScope: CoroutineScope,
    private val habitTracksRepository: HabitTracksRepository,
    private val habitTrackIntervalValidator: HabitTrackIntervalValidator,
    private val habitId: Habit.Id
) {

    private val creationState = MutableStateFlow<CreationState>(CreationState.NotExecuted())
    private val intervalState = MutableStateFlow<HabitTrack.Interval?>(null)
    private val dailyCountState = MutableStateFlow<HabitTrack.DailyCount?>(null)
    private val commentState = MutableStateFlow<HabitTrack.Comment?>(null)

    val state = combine(
        creationState,
        intervalState,
        intervalState.map { it?.let { habitTrackIntervalValidator.validate(it) } },
        dailyCountState,
        commentState
    ) { creationState, interval, validatedInterval, dailyCount, comment ->
        when (creationState) {
            is CreationState.NotExecuted -> State.Input(
                interval,
                validatedInterval,
                dailyCount,
                comment,
                creationAllowed = interval != null
                        && validatedInterval is Correct
                        && dailyCount != null
            )

            is CreationState.Executing -> State.Creating()
            is CreationState.Executed -> State.Created()
        }
    }.stateIn(
        coroutineScope,
        SharingStarted.WhileSubscribed(),
        State.Input(
            interval = null,
            validatedInterval = null,
            dailyCount = null,
            comment = null,
            creationAllowed = false
        )
    )

    fun startCreation() {
        val state = state.value

        require(state is State.Input)
        require(state.creationAllowed)
        requireNotNull(state.interval)
        require(state.validatedInterval is Correct)
        requireNotNull(state.dailyCount)

        creationState.value = CreationState.Executing()

        coroutineScope.launch {
            habitTracksRepository.insertHabitTrack(
                habitId,
                state.validatedInterval,
                state.dailyCount,
                state.comment
            )
            creationState.value = CreationState.Executed()
        }
    }

    fun updateInterval(interval: HabitTrack.Interval) {
        require(state.value is State.Input)
        intervalState.value = interval
    }

    fun updateDailyCount(dailyCount: HabitTrack.DailyCount) {
        require(state.value is State.Input)
        dailyCountState.value = dailyCount
    }

    fun updateComment(comment: HabitTrack.Comment) {
        require(state.value is State.Input)
        commentState.value = comment
    }

    sealed class State {
        data class Input(
            val interval: HabitTrack.Interval?,
            val validatedInterval: Validated<HabitTrack.Interval, HabitTrackIntervalValidator.IncorrectReason>?,
            val dailyCount: HabitTrack.DailyCount?,
            val comment: HabitTrack.Comment?,
            val creationAllowed: Boolean
        ) : State()

        class Creating : State()
        class Created : State()
    }

    private sealed class CreationState {
        class NotExecuted : CreationState()
        class Executing : CreationState()
        class Executed : CreationState()
    }
}