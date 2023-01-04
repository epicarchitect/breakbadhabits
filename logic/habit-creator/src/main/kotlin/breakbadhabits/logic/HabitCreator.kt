package breakbadhabits.logic

import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitTrack

class HabitCreator internal constructor(
    private val delegate: HabitCreatorModule.Delegate
) {

    suspend fun createHabit(
        name: CorrectHabitNewNewName,
        iconResource: Habit.IconResource,
        countability: HabitCountability,
        firstTrackInterval: CorrectHabitTrackInterval
    ) {
        val newHabit = delegate.insertHabit(
            name = name.data,
            iconResource = iconResource,
            countability = Habit.Countability(
                idCountable = countability is HabitCountability.Countable
            )
        )

        delegate.insertHabitTrack(
            habitId = newHabit.id,
            range = firstTrackInterval.data,
            dailyCount = when (countability) {
                is HabitCountability.Countable -> {
                    countability.averageDailyCount
                }

                is HabitCountability.Uncountable -> {
                    HabitTrack.DailyCount(1.0)
                }
            },
            comment = null
        )
    }
}