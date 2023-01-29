package breakbadhabits.app.presentation.habit.track.details

import breakbadhabits.framework.viewmodel.ViewModel
import androidx.lifecycle.viewModelScope
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.logic.habit.track.provider.HabitTrackProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HabitTrackViewModel(
    habitTrackProvider: HabitTrackProvider,
    habitTrackId: HabitTrack.Id
) : ViewModel() {

    val state = habitTrackProvider.provideById(habitTrackId).map {
        if (it == null) State.NotExist()
        else State.Loaded(it)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), State.Loading())

    sealed class State {
        class Loading : State()
        class Loaded(val habitTrack: HabitTrack) : State()
        class NotExist : State()
    }
}