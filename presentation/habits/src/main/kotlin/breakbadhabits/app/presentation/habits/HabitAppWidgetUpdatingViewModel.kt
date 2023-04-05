package breakbadhabits.app.presentation.habits

import androidx.lifecycle.viewModelScope
import breakbadhabits.app.logic.habits.HabitProvider
import breakbadhabits.app.logic.habits.HabitWidgetDeleter
import breakbadhabits.app.logic.habits.HabitWidgetProvider
import breakbadhabits.app.logic.habits.HabitWidgetUpdater
import breakbadhabits.app.logic.habits.model.HabitWidget
import breakbadhabits.foundation.controller.MultiSelectionController
import breakbadhabits.foundation.controller.SingleRequestController
import breakbadhabits.foundation.controller.ValidatedInputController
import breakbadhabits.foundation.viewmodel.ViewModel
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
                habitIds = habitsSelectionController.state.value.items.filter {
                    it.value
                }.map { it.key.id }
            )
        },
        isAllowedFlow = combine(
            initialConfig,
            titleInputController.state,
            habitsSelectionController.state
        ) { initial, title, habitsSelection ->
            val habitIdsInput = habitsSelection.items.filter {
                it.value
            }.keys.map { it.id }.sortedBy { it }

            val isChanged = initial?.title != title.input
                    || initial.habitIds.sortedBy { it } != habitIdsInput

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
            habitsSelectionController.checkList(
                habits.filter { config.habitIds.contains(it.id) }
            )
        }
    }
}