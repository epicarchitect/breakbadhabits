package breakbadhabits.logic

import breakbadhabits.entity.HabitTrack
import kotlinx.coroutines.flow.Flow

class HabitTrackProviderModule(private val delegate: Delegate) {

    fun createHabitTrackProvider() = HabitTrackProvider(delegate)

    interface Delegate {
        fun habitTrackFlow(id: HabitTrack.Id): Flow<HabitTrack?>
        suspend fun getHabitTrack(id: HabitTrack.Id): HabitTrack?
    }
}