package breakbadhabits.feature.habits.presentation

import breakbadhabits.entity.HabitTrack
import breakbadhabits.feature.habits.model.HabitTracksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HabitTrackDeletionViewModel internal constructor(
    private val coroutineScope: CoroutineScope,
    private val habitTracksRepository: HabitTracksRepository,
    private val id: HabitTrack.Id
) {
    private val mutableState = MutableStateFlow<State>(State.Ready())
    val state = mutableState.asStateFlow()

    fun startDeleting() {
        require(mutableState.value is State.Ready)
        mutableState.value = State.InProgress()

        coroutineScope.launch {
            habitTracksRepository.deleteHabitTrackById(id)
            mutableState.value = State.HabitTrackDeleted()
        }
    }

    fun dispose() {
        coroutineScope.cancel()
    }

    sealed class State {
        class Ready : State()
        class InProgress : State()
        class HabitTrackDeleted : State()
    }
}