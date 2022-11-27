package breakbadhabits.logic

import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitTrack
import breakbadhabits.logic.dependecy.repository.HabitTracksRepository
import breakbadhabits.logic.dependecy.repository.HabitsRepository
import kolmachikhin.alexander.validation.Correct

class HabitCreator(
    private val habitsRepository: HabitsRepository,
    private val habitTracksRepository: HabitTracksRepository
) {

    suspend fun createHabit(
        name: Correct<Habit.Name>,
        iconResource: Habit.IconResource,
        countability: HabitCountability,
        firstTrackInterval: Correct<HabitTrack.Interval>
    ) {
        val newHabit = habitsRepository.insertHabit(
            name = name.data,
            iconResource = iconResource,
            countability = Habit.Countability(
                idCountable = countability is HabitCountability.Countable
            )
        )

        habitTracksRepository.insertHabitTrack(
            habitId = newHabit.id,
            interval = firstTrackInterval.data,
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

    sealed class HabitCountability {
        class Countable(val averageDailyCount: HabitTrack.DailyCount) : HabitCountability()
        class Uncountable : HabitCountability()
    }
}