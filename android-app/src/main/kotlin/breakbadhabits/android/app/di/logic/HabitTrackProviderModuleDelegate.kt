package breakbadhabits.android.app.di.logic

import breakbadhabits.android.app.repository.HabitTracksRepository
import breakbadhabits.entity.HabitTrack
import breakbadhabits.logic.HabitTrackProviderModule

class HabitTrackProviderModuleDelegate(
    private val habitTracksRepository: HabitTracksRepository
) : HabitTrackProviderModule.Delegate {
    override fun habitTrackFlow(id: HabitTrack.Id) =
        habitTracksRepository.habitTrackFlowById(id)

    override suspend fun getHabitTrack(id: HabitTrack.Id) =
        habitTracksRepository.getHabitTrackById(id)
}