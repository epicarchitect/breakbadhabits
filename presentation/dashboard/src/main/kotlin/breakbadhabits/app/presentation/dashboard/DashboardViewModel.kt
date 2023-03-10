package breakbadhabits.app.presentation.dashboard

import androidx.lifecycle.viewModelScope
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitAbstinence
import breakbadhabits.app.logic.habits.provider.HabitAbstinenceProvider
import breakbadhabits.app.logic.habits.provider.HabitProvider
import breakbadhabits.foundation.controller.LoadingController
import breakbadhabits.foundation.viewmodel.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class DashboardViewModel(
    habitProvider: HabitProvider,
    private val habitAbstinenceProvider: HabitAbstinenceProvider
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val habitItemsController = LoadingController(
        coroutineScope = viewModelScope,
        flow = habitProvider.provideHabitsFlow().flatMapLatest { habits ->
            if (habits.isEmpty()) flowOf(emptyList())
            else combine(
                habits.map { habit ->
                    habitAbstinenceProvider.provideFlowById(habit.id)
                }
            ) { abstinenceList ->
                habits.mapIndexed { index, habit ->
                    DashboardHabitItem(
                        habit,
                        abstinenceList[index]
                    )
                }
            }
        }
    )
}