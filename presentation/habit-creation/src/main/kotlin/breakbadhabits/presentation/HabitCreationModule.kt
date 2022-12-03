package breakbadhabits.presentation

import breakbadhabits.logic.HabitCreatorModule
import breakbadhabits.logic.HabitIconsProviderModule

class HabitCreationModule(
    private val creatorModule: HabitCreatorModule,
    private val iconsProviderModule: HabitIconsProviderModule
) {

    fun createHabitCreationViewModel() = HabitCreationViewModel(
        habitCreator = creatorModule.createHabitCreator(),
        nameValidator = creatorModule.createHabitNewNameValidator(),
        trackIntervalValidator = creatorModule.createHabitTrackIntervalValidator(),
        habitIconsProvider = iconsProviderModule.createHabitIconsProvider()
    )
}