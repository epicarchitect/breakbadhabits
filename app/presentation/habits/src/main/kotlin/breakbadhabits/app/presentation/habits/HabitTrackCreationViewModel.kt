package breakbadhabits.app.presentation.habits

import androidx.lifecycle.viewModelScope
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.logic.habits.creator.HabitTrackCreator
import breakbadhabits.foundation.viewmodel.ViewModel
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
    private val valueState = MutableStateFlow<HabitTrack.EventCount?>(null)
    private val commentState = MutableStateFlow<HabitTrack.Comment?>(null)

    val state = combine(
        creationState,
        rangeState,
        valueState,
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
            value = null,
            comment = null,
            creationAllowed = false
        )
    )

    fun startCreation() {
        val state = state.value

        require(state is State.Input)
        require(state.creationAllowed)
        requireNotNull(state.range)
        requireNotNull(state.value)

        creationState.value = CreationState.Executing()

        viewModelScope.launch {
            habitTrackCreator.createHabitTrack(
                habitId,
                state.range,
                state.value,
                state.comment
            )
            creationState.value = CreationState.Executed()
        }
    }

    fun updateInterval(range: HabitTrack.Range) {
        require(state.value is State.Input)
        rangeState.value = range
    }

    fun updateDailyCount(value: HabitTrack.EventCount) {
        require(state.value is State.Input)
        valueState.value = value
    }

    fun updateComment(comment: HabitTrack.Comment) {
        require(state.value is State.Input)
        commentState.value = comment
    }

    sealed class State {
        data class Input(
            val range: HabitTrack.Range?,
            val value: HabitTrack.EventCount?,
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