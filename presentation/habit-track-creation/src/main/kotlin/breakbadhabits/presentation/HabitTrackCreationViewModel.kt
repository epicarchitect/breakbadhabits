package breakbadhabits.presentation

import breakbadhabits.extension.viewmodel.ViewModel
import androidx.lifecycle.viewModelScope
import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitTrack
import breakbadhabits.logic.HabitTrackCreator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HabitTrackCreationViewModel(
    private val habitTrackCreator: HabitTrackCreator,
    private val habitId: Habit.Id
) : ViewModel() {

    private val creationState = MutableStateFlow<CreationState>(CreationState.NotExecuted())
    private val rangeState = MutableStateFlow<HabitTrack.Range?>(null)
    private val dailyCountState = MutableStateFlow<HabitTrack.DailyCount?>(null)
    private val commentState = MutableStateFlow<HabitTrack.Comment?>(null)

    val state = combine(
        creationState,
        rangeState,
        dailyCountState,
        commentState
    ) { creationState, interval, dailyCount, comment ->
        when (creationState) {
            is CreationState.NotExecuted -> State.Input(
                interval,
                dailyCount,
                comment,
                creationAllowed = interval != null && dailyCount != null
            )

            is CreationState.Executing -> State.Creating()
            is CreationState.Executed -> State.Created()
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        State.Input(
            range = null,
            dailyCount = null,
            comment = null,
            creationAllowed = false
        )
    )

    fun startCreation() {
        val state = state.value

        require(state is State.Input)
        require(state.creationAllowed)
        requireNotNull(state.range)
        requireNotNull(state.dailyCount)

        creationState.value = CreationState.Executing()

        viewModelScope.launch {
            habitTrackCreator.createHabitTrack(
                habitId,
                state.range,
                state.dailyCount,
                state.comment
            )
            creationState.value = CreationState.Executed()
        }
    }

    fun updateInterval(range: HabitTrack.Range) {
        require(state.value is State.Input)
        rangeState.value = range
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
            val range: HabitTrack.Range?,
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