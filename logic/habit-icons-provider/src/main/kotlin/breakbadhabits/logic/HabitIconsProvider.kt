package breakbadhabits.logic

import breakbadhabits.logic.dependecy.repository.HabitIconsRepository

class HabitIconsProvider(private val habitIconsRepository: HabitIconsRepository) {

    fun provide() = habitIconsRepository.getHabitIcons()

}