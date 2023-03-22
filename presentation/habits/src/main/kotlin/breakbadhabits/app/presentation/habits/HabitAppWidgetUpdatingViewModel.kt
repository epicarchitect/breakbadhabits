package breakbadhabits.app.presentation.habits

import breakbadhabits.app.entity.HabitAppWidgetConfig
import breakbadhabits.app.logic.habits.appWidgetConfig.HabitAppWidgetConfigUpdater
import breakbadhabits.app.logic.habits.HabitProvider
import breakbadhabits.foundation.viewmodel.ViewModel

class HabitAppWidgetUpdatingViewModel(
    private val habitProvider: HabitProvider,
    private val habitAppWidgetConfigUpdater: HabitAppWidgetConfigUpdater,
    private val id: HabitAppWidgetConfig.Id
) : ViewModel() {

}