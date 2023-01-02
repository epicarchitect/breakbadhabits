package breakbadhabits.presentation

import breakbadhabits.entity.HabitTrack
import breakbadhabits.logic.HabitTrackProviderModule

class HabitTrackModule(
    private val habitTrackProviderModule: HabitTrackProviderModule
) {

    fun createHabitTrackViewModel(id: HabitTrack.Id) = HabitTrackViewModel(
        habitTrackProvider = habitTrackProviderModule.createHabitTrackProvider(),
        habitTrackId = id
    )
}