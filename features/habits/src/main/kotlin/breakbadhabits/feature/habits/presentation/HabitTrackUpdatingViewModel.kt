package breakbadhabits.feature.habits.presentation

import breakbadhabits.entity.HabitTrack
import breakbadhabits.extension.coroutines.flow.combine
import breakbadhabits.feature.habits.model.HabitTracksRepository
import breakbadhabits.feature.habits.validator.HabitTrackIntervalValidator
import kolmachikhin.alexander.validation.Correct
import kolmachikhin.alexander.validation.Validated
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HabitTrackUpdatingViewModel internal constructor(
    private val coroutineScope: CoroutineScope,
    private val habitTracksRepository: HabitTracksRepository,
    private val habitTrackIntervalValidator: HabitTrackIntervalValidator,
    private val id: HabitTrack.Id
) {
    private val initialTrackLoadState = MutableStateFlow<InitialTrackLoadState?>(null)
    private val updatingState = MutableStateFlow<UpdatingState>(UpdatingState.NotExecuted())
    private val intervalState = MutableStateFlow<HabitTrack.Interval?>(null)
    private val dailyCountState = MutableStateFlow<HabitTrack.DailyCount?>(null)
    private val commentState = MutableStateFlow<HabitTrack.Comment?>(null)

    val state = combine(
        initialTrackLoadState,
        updatingState,
        intervalState.filterNotNull(),
        intervalState.filterNotNull().map { habitTrackIntervalValidator.validate(it) },
        dailyCountState.filterNotNull(),
        commentState
    ) { initialTrackLoadState, updatingState, interval, validatedInterval, dailyCount, comment ->
        when (initialTrackLoadState) {
            is InitialTrackLoadState.Loaded -> {
                State.Input(
                    updatingState = updatingState,
                    interval = interval,
                    validatedInterval = validatedInterval,
                    dailyCount = dailyCount,
                    comment = comment,
                    updatingAllowed = updatingState is UpdatingState.NotExecuted
                            && validatedInterval is Correct
                            && (interval != initialTrackLoadState.track.interval
                            || dailyCount != initialTrackLoadState.track.dailyCount
                            || comment != initialTrackLoadState.track.comment)
                )
            }

            is InitialTrackLoadState.NotExist -> {
                error("Nice, i dont wont fix this right now, ok yes?")
            }

            else -> {
                State.LoadingInitialTrack()
            }
        }
    }.stateIn(
        coroutineScope,
        SharingStarted.WhileSubscribed(),
        State.LoadingInitialTrack()
    )

    init {
        loadInitialTrack()
    }

    fun startUpdating() {
        val state = state.value

        require(state is State.Input)
        require(state.updatingAllowed)
        require(state.validatedInterval is Correct)
        require(state.updatingState is UpdatingState.NotExecuted)

        updatingState.value = UpdatingState.Executing()

        coroutineScope.launch {
            habitTracksRepository.updateHabitTrack(
                id,
                state.validatedInterval,
                state.dailyCount,
                state.comment
            )
            updatingState.value = UpdatingState.Executed()
        }
    }

    fun updateInterval(interval: HabitTrack.Interval) {
        require(state.value is HabitTrackCreationViewModel.State.Input)
        intervalState.value = interval
    }

    fun updateDailyCount(dailyCount: HabitTrack.DailyCount) {
        require(state.value is HabitTrackCreationViewModel.State.Input)
        dailyCountState.value = dailyCount
    }

    fun updateComment(comment: HabitTrack.Comment) {
        require(state.value is HabitTrackCreationViewModel.State.Input)
        commentState.value = comment
    }


    private fun loadInitialTrack() {
        initialTrackLoadState.value = InitialTrackLoadState.Loading()
        coroutineScope.launch {
            val track = habitTracksRepository.getHabitTrackById(id)
            if (track == null) {
                initialTrackLoadState.value = InitialTrackLoadState.NotExist()
            } else {
                intervalState.value = track.interval
                dailyCountState.value = track.dailyCount
                commentState.value = track.comment
                initialTrackLoadState.value = InitialTrackLoadState.Loaded(track)
            }
        }
    }

    sealed class State {
        class LoadingInitialTrack : State()
        data class Input(
            val updatingState: UpdatingState,
            val interval: HabitTrack.Interval,
            val validatedInterval: Validated<HabitTrack.Interval, HabitTrackIntervalValidator.IncorrectReason>,
            val dailyCount: HabitTrack.DailyCount,
            val comment: HabitTrack.Comment?,
            val updatingAllowed: Boolean
        )
    }

    sealed class UpdatingState {
        class NotExecuted : UpdatingState()
        class Executing : UpdatingState()
        class Executed : UpdatingState()
    }

    private sealed class InitialTrackLoadState {
        class NotExist : InitialTrackLoadState()
        class Loading : InitialTrackLoadState()
        class Loaded(val track: HabitTrack) : InitialTrackLoadState()
    }
}