package epicarchitect.breakbadhabits.presentation.dashboard

import epicarchitect.breakbadhabits.di.declaration.logic.HabitsLogicModule
import epicarchitect.breakbadhabits.foundation.controller.DataFlowController
import epicarchitect.breakbadhabits.foundation.controller.SingleRequestController
import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineScopeOwner
import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineScopeOwnersManager
import epicarchitect.breakbadhabits.logic.habits.deleter.HabitDeleter
import epicarchitect.breakbadhabits.logic.habits.model.Habit
import epicarchitect.breakbadhabits.logic.habits.model.HabitAbstinence
import epicarchitect.breakbadhabits.logic.habits.provider.HabitAbstinenceProvider
import epicarchitect.breakbadhabits.logic.habits.provider.HabitProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class DashboardViewModel(
    override val coroutineScope: CoroutineScope,
    private val habitsModule: HabitsLogicModule,
) : CoroutineScopeOwner {

    val openHabitDetails = Channel<Int>()
    val resetHabit = Channel<Int>()

    val items = CoroutineScopeOwnersManager(
        flow = habitsModule.habitProvider.habitsFlow().flatMapLatest { habits ->
            if (habits.isEmpty()) {
                flowOf(emptyList())
            } else {
                combine(
                    habits.map { habit ->
                        habitsModule.habitAbstinenceProvider.currentAbstinenceFlow(habit.id)
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
        },
        map = { scope, item ->
            HabitItemViewModel(
                coroutineScope = scope,
                habit = item.habit,
                abstinence = item.abstinence,
                onNavigateToHabit = {
                    openHabitDetails.trySend(item.habit.id)
                },
                onReset = {
                    resetHabit.trySend(item.habit.id)
                }
            )
        }
    )

    val itemsLoadingController = DataFlowController(items)

    data class HabitItemViewModel(
        override val coroutineScope: CoroutineScope,
        val habit: Habit,
        val abstinence: HabitAbstinence?,
        private val onNavigateToHabit: () -> Unit,
        private val onReset: () -> Unit
    ) : CoroutineScopeOwner {
        val resetController = SingleRequestController(
            request = {
                onReset()
                // event to parent
            }
        )
        val openDetailsController = SingleRequestController(
            request = {
                onNavigateToHabit()
                // event to parent?
            }
        )
    }
}