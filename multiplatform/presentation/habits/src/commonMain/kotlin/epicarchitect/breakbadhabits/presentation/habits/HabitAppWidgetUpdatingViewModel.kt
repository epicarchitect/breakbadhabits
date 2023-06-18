package epicarchitect.breakbadhabits.presentation.habits

import epicarchitect.breakbadhabits.foundation.controller.MultiSelectionController
import epicarchitect.breakbadhabits.foundation.controller.SingleRequestController
import epicarchitect.breakbadhabits.foundation.controller.ValidatedInputController
import epicarchitect.breakbadhabits.foundation.controller.requireSelectedItems
import epicarchitect.breakbadhabits.foundation.viewmodel.ViewModel
import epicarchitect.breakbadhabits.logic.habits.deleter.HabitWidgetDeleter
import epicarchitect.breakbadhabits.logic.habits.model.Habit
import epicarchitect.breakbadhabits.logic.habits.model.HabitWidget
import epicarchitect.breakbadhabits.logic.habits.provider.HabitProvider
import epicarchitect.breakbadhabits.logic.habits.provider.HabitWidgetProvider
import epicarchitect.breakbadhabits.logic.habits.updater.HabitWidgetUpdater
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HabitAppWidgetUpdatingViewModel(
    habitProvider: HabitProvider,
    habitWidgetProvider: HabitWidgetProvider,
    habitWidgetUpdater: HabitWidgetUpdater,
    habitWidgetDeleter: HabitWidgetDeleter,
    habitWidgetId: Int
) : ViewModel() {

    private val initialConfig = MutableStateFlow<HabitWidget?>(null)

    val titleInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = "",
        validation = { null }
    )

    val habitsSelectionController = MultiSelectionController(
        coroutineScope = viewModelScope,
        itemsFlow = habitProvider.habitsFlow()
    )

    val updatingController = SingleRequestController(
        coroutineScope = viewModelScope,
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

    val deletionController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            habitWidgetDeleter.deleteById(habitWidgetId)
        }
    )

    init {
        viewModelScope.launch {
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
