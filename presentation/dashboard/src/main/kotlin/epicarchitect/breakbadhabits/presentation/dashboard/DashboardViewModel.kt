package epicarchitect.breakbadhabits.presentation.dashboard

import androidx.lifecycle.viewModelScope
import epicarchitect.breakbadhabits.foundation.controller.LoadingController
import epicarchitect.breakbadhabits.foundation.viewmodel.ViewModel
import epicarchitect.breakbadhabits.logic.habits.provider.HabitAbstinenceProvider
import epicarchitect.breakbadhabits.logic.habits.provider.HabitProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class DashboardViewModel(
    habitProvider: HabitProvider,
    habitAbstinenceProvider: HabitAbstinenceProvider
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val itemsLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = habitProvider.habitsFlow().flatMapLatest { habits ->
            if (habits.isEmpty()) flowOf(emptyList())
            else combine(
                habits.map { habit ->
                    habitAbstinenceProvider.currentAbstinenceFlow(habit.id)
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