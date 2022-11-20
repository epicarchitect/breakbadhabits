package breakbadhabits.feature.habits.presentation

import breakbadhabits.entity.HabitTrack
import breakbadhabits.feature.habits.model.HabitTracksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HabitTrackViewModel(
    private val coroutineScope: CoroutineScope,
    habitTracksRepository: HabitTracksRepository,
    id: HabitTrack.Id
) {

    val state = habitTracksRepository.habitTrackFlowById(id).map {
        if (it == null) State.NotExist()
        else State.Loaded(it)
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), State.Loading())

    fun dispose() {
        coroutineScope.cancel()
    }

    sealed class State {
        class Loading : State()
        class Loaded(val track: HabitTrack)
        class NotExist : State()
    }
}