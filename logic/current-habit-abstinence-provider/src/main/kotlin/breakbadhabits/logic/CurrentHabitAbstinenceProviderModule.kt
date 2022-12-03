package breakbadhabits.logic

import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitTrack
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

class CurrentHabitAbstinenceProviderModule(
    private val delegate: Delegate
) {

    fun createCurrentHabitAbstinenceProvider() = CurrentHabitAbstinenceProvider(delegate)

    interface Delegate {
        fun habitTrackFlowByHabitIdAndLastByTime(id: Habit.Id): Flow<HabitTrack?>
        fun currentTimeFlow(): Flow<LocalDateTime>
    }
}