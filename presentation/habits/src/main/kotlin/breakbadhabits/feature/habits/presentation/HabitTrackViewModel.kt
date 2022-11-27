package breakbadhabits.feature.habits.presentation

import breakbadhabits.entity.HabitTrack
import breakbadhabits.feature.EpicViewModel
import breakbadhabits.feature.habits.model.HabitTracksRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HabitTrackViewModel(
    habitTracksRepository: HabitTracksRepository,
    id: HabitTrack.Id
) : EpicViewModel() {

    val state = habitTracksRepository.habitTrackFlowById(id).map {
        if (it == null) State.NotExist()
        else State.Loaded(it)
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), State.Loading())

    sealed class State {
        class Loading : State()
        class Loaded(val track: HabitTrack)
        class NotExist : State()
    }
}