package epicarchitect.breakbadhabits.presentation.app

import epicarchitect.breakbadhabits.di.declaration.AppModule
import epicarchitect.breakbadhabits.foundation.controller.SingleRequestController
import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineScopeOwner
import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineScopeOwnersManager
import epicarchitect.breakbadhabits.presentation.dashboard.DashboardViewModel
import epicarchitect.breakbadhabits.presentation.habits.HabitDetailsViewModel
import epicarchitect.breakbadhabits.presentation.habits.HabitTrackCreationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AppViewModel(
    private
    override val coroutineScope: CoroutineScope,
    val appModule: AppModule
) : CoroutineScopeOwner {

    val navigation = CoroutineScopeOwnersManager<CoroutineScopeOwner>()

    init {
        navigation.add {
            DashboardViewModel(
                coroutineScope = it,
                habitsModule = appModule.logic.habits
            )
        }.bind()
    }

    private fun DashboardViewModel.bind() {
        openHabitDetails.receiveAsFlow().onEach { habitId ->
            navigation.add {
                HabitDetailsViewModel(
                    coroutineScope = it,
                    habitsLogicModule = appModule.logic.habits,
                    dateTimeLogicModule = appModule.logic.dateTime,
                    habitId = habitId
                )
            }.bind()
        }.launchIn(coroutineScope)

        resetHabit.receiveAsFlow().onEach { habitId ->
            navigation.add { scope ->
                HabitTrackCreationViewModel(
                    coroutineScope = scope,
                    appModule = appModule,
                    habitId = habitId
                )
            }.bind()
        }.launchIn(coroutineScope)
    }

    private fun HabitTrackCreationViewModel.bind() {
        coroutineScope.launch {
            creationController.state.first {
                it.requestState is SingleRequestController.RequestState.Executed
            }
            navigation.remove(this@bind)
        }
    }

    private fun HabitDetailsViewModel.bind() {
        coroutineScope.launch {
            addTrackController.state.first { it.requestState is SingleRequestController.RequestState.Executed }
            navigation.add { scope ->
                HabitTrackCreationViewModel(
                    coroutineScope = scope,
                    appModule = appModule,
                    habitId = habitId
                )
            }.bind()
        }
    }
}