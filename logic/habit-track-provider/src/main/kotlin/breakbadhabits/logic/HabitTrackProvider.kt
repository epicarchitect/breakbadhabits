package breakbadhabits.logic

import breakbadhabits.entity.HabitTrack

class HabitTrackProvider internal constructor(
    private val delegate: HabitTrackProviderModule.Delegate
) {

    fun provideFlow(id: HabitTrack.Id) = delegate.habitTrackFlow(id)

    suspend fun provide(id: HabitTrack.Id) = delegate.getHabitTrack(id)

}