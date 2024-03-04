package epicarchitect.breakbadhabits.presentation.habits

import epicarchitect.breakbadhabits.foundation.controller.MultiSelectionController
import epicarchitect.breakbadhabits.foundation.controller.SingleRequestController
import epicarchitect.breakbadhabits.foundation.controller.ValidatedInputController
import epicarchitect.breakbadhabits.foundation.controller.requireSelectedItems
import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineScopeOwner
import epicarchitect.breakbadhabits.logic.habits.deleter.HabitWidgetDeleter
import epicarchitect.breakbadhabits.logic.habits.model.Habit
import epicarchitect.breakbadhabits.logic.habits.model.HabitWidget
import epicarchitect.breakbadhabits.logic.habits.provider.HabitProvider
import epicarchitect.breakbadhabits.logic.habits.provider.HabitWidgetProvider
import epicarchitect.breakbadhabits.logic.habits.updater.HabitWidgetUpdater
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HabitAppWidgetUpdatingViewModel(
    override val coroutineScope: CoroutineScope,
    habitProvider: HabitProvider,
    habitWidgetProvider: HabitWidgetProvider,
    habitWidgetUpdater: HabitWidgetUpdater,
    habitWidgetDeleter: HabitWidgetDeleter,
    habitWidgetId: Int
) : CoroutineScopeOwner {

    private val initialConfig = MutableStateFlow<HabitWidget?>(null)

    val titleInputController = ValidatedInputController(
        initialInput = "",
        validation = { null }
    )

    val habitsSelectionController = MultiSelectionController(habitProvider.habitsFlow())

    val updatingController = SingleRequestController(
        request = {
            habitWidgetUpdater.updateAppWidget(
                id = habitWidgetId,
                title = titleInputController.state.value.input,
                habitIds = habitsSelectionController.requireSelectedItems().map(Habit::id)
            )
        },
        isAllowedFlow = combine(
            initialConfig,
            titleInputController.state,
            habitsSelectionController.state
        ) { initial, title, habitsSelection ->
            if (habitsSelection !is MultiSelectionController.State.Loaded) return@combine false

            val habitIdsInput = habitsSelection.selectedItems.map(Habit::id).sorted()

            val isChanged = initial?.title != title.input ||
                initial.habitIds.sorted() != habitIdsInput

            isChanged && habitIdsInput.isNotEmpty()
        }
    )

    val deletionController = SingleRequestController {
        habitWidgetDeleter.deleteById(habitWidgetId)
    }

    init {
        coroutineScope.launch {
            val config = checkNotNull(habitWidgetProvider.provideFlowById(habitWidgetId).first())
            val habits = habitProvider.habitsFlow().first()
            initialConfig.value = config
            titleInputController.changeInput(config.title)
            habitsSelectionController.toggleItems(
                habits.filter { config.habitIds.contains(it.id) }
            )
        }
    }
}