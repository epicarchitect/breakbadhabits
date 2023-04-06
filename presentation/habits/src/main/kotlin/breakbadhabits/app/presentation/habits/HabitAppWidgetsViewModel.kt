package breakbadhabits.app.presentation.habits

import androidx.lifecycle.viewModelScope
import breakbadhabits.app.logic.habits.provider.HabitProvider
import breakbadhabits.app.logic.habits.provider.HabitWidgetProvider
import breakbadhabits.app.logic.habits.model.Habit
import breakbadhabits.app.logic.habits.model.HabitWidget
import breakbadhabits.foundation.controller.LoadingController
import breakbadhabits.foundation.viewmodel.ViewModel
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
            if (configs.isEmpty()) flowOf(emptyList())
            else combine(
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
    )

    data class Item(
        val widget: HabitWidget,
        val habits: List<Habit>
    )
}
