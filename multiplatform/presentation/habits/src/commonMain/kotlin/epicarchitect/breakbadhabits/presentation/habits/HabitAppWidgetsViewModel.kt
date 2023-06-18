package epicarchitect.breakbadhabits.presentation.habits

import epicarchitect.breakbadhabits.foundation.controller.LoadingController
import epicarchitect.breakbadhabits.foundation.viewmodel.ViewModel
import epicarchitect.breakbadhabits.logic.habits.model.Habit
import epicarchitect.breakbadhabits.logic.habits.model.HabitWidget
import epicarchitect.breakbadhabits.logic.habits.provider.HabitProvider
import epicarchitect.breakbadhabits.logic.habits.provider.HabitWidgetProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class HabitAppWidgetsViewModel(
    habitWidgetProvider: HabitWidgetProvider,
    habitProvider: HabitProvider
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val itemsController = LoadingController(
        coroutineScope = viewModelScope,
        flow = habitWidgetProvider.provideAllFlow().flatMapLatest { configs ->
            if (configs.isEmpty()) {
                flowOf(emptyList())
            } else {
                combine(
                    configs.map { config ->
                        combine(config.habitIds.map(habitProvider::habitFlow)) { habits ->
                            Item(
                                widget = config,
                                habits = habits.filterNotNull()
                            )
                        }
                    }
                ) {
                    it.toList()
                }
            }
        }
    )

    data class Item(
        val widget: HabitWidget,
        val habits: List<Habit>
    )
}
