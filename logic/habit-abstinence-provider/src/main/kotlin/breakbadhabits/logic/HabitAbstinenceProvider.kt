package breakbadhabits.logic

import breakbadhabits.database.AppDatabase
import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitAbstinence
import breakbadhabits.extension.datetime.millisToLocalDateTime
import com.squareup.sqldelight.runtime.coroutines.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class HabitAbstinenceProvider(
    private val appDatabase: AppDatabase,
    private val timeProvider: TimeProvider
) {
    fun provideCurrentHabitAbstinenceFlow(habitId: Habit.Id) = combine(
        appDatabase.habitTrackQueries
            .selectMaxRangeEndByHabitId(habitId.value)
            .asFlow()
            .map {
                it.executeAsOneOrNull()?.max?.millisToLocalDateTime()
            },
        timeProvider.currentTimeFlow()
    ) { lastRangeEnd, currentTime ->
        lastRangeEnd ?: return@combine null
        HabitAbstinence(
            habitId = habitId,
            range = HabitAbstinence.Range(lastRangeEnd..currentTime)
        )
    }
}