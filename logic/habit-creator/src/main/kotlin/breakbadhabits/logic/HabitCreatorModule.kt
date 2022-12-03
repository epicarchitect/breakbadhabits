package breakbadhabits.logic

import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitTrack
import kotlinx.datetime.LocalDateTime

class HabitCreatorModule(private val delegate: Delegate) {

    fun createHabitCreator() = HabitCreator(delegate)

    fun createHabitNewNameValidator() = HabitNewNameValidator(delegate)

    fun createHabitTrackIntervalValidator() = HabitTrackIntervalValidator(delegate)
    
    interface Delegate {
        suspend fun insertHabit(
            name: Habit.Name,
            iconResource: Habit.IconResource,
            countability: Habit.Countability
        ): Habit

        suspend fun insertHabitTrack(
            habitId: Habit.Id,
            interval: HabitTrack.Interval,
            dailyCount: HabitTrack.DailyCount,
            comment: HabitTrack.Comment?
        )

        suspend fun habitNameExists(name: Habit.Name): Boolean

        fun getCurrentTime(): LocalDateTime

        fun getMaxHabitNameLength(): Int
    }
}