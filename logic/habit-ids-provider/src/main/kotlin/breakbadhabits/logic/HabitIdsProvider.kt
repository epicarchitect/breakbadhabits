package breakbadhabits.logic

import breakbadhabits.logic.dependecy.repository.HabitsRepository

class HabitIdsProvider(private val habitsRepository: HabitsRepository) {

    fun provideFlow() = habitsRepository.habitIdsFlow()

}