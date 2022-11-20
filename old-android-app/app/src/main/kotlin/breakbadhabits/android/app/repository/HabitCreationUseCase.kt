package breakbadhabits.android.app.repository

import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitTrack
import kotlinx.datetime.LocalDate

class HabitCreationUseCase(
    private val habitsRepository: HabitsRepository,
    private val habitTracksRepository: HabitTracksRepository
) {

    suspend fun invoke(
        habitName: String,
        habitIconId: Int,
        isCountable: Boolean,
        firstEventDay: LocalDate,
        lastEventDay: LocalDate,
        averageDayValue: Int
    ) {
        val habitId = habitsRepository.generateNextId()
        val habitEventId = habitTracksRepository.generateNextId()

        habitsRepository.saveHabit(
            Habit(
                habitId,
                habitName,
                habitIconId,
                isCountable
            )
        )

        habitTracksRepository.saveHabitTrack(
            HabitTrack(
                habitEventId,
                habitId,
                firstEventDay,
                lastEventDay,
                averageDayValue,
                null
            )
        )
    }
}