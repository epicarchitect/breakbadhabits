package breakbadhabits.android.app.di.logic

import breakbadhabits.android.app.repository.HabitIconRepository
import breakbadhabits.logic.HabitIconsProviderModule

class HabitIconProviderModuleDelegate(
    private val habitIconRepository: HabitIconRepository
) : HabitIconsProviderModule.Delegate {
    override fun getHabitIcons() = habitIconRepository.list
}