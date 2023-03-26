package breakbadhabits.app.presentation.habits

import androidx.lifecycle.viewModelScope
import breakbadhabits.app.logic.habits.HabitProvider
import breakbadhabits.app.logic.habits.appWidgetConfig.HabitAppWidgetConfigProvider
import breakbadhabits.app.logic.habits.entity.Habit
import breakbadhabits.app.logic.habits.entity.HabitAppWidgetConfig
import breakbadhabits.foundation.controller.LoadingController
import breakbadhabits.foundation.viewmodel.ViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class HabitAppWidgetsViewModel(
    habitAppWidgetConfigProvider: HabitAppWidgetConfigProvider,
    habitProvider: HabitProvider
) : ViewModel() {

    val itemsController = LoadingController(
        coroutineScope = viewModelScope,
        flow = habitAppWidgetConfigProvider.provideAllFlow().flatMapLatest { configs ->
            if (configs.isEmpty()) flowOf(emptyList())
            else combine(
                configs.map { config ->
                    combine(config.habitIds.map(habitProvider::habitFlow)) { habits ->
                        Item(
                            config = config,
                            habits = habits.filterNotNull()
                        )
                    }
                }
            ) {
                it.toList()
            }
        }
    )

    data class Item(
        val config: HabitAppWidgetConfig,
        val habits: List<Habit>
    )
}
