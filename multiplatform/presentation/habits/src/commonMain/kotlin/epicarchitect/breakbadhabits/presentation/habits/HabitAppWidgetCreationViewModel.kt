package epicarchitect.breakbadhabits.presentation.habits

import epicarchitect.breakbadhabits.foundation.controller.InputController
import epicarchitect.breakbadhabits.foundation.controller.MultiSelectionController
import epicarchitect.breakbadhabits.foundation.controller.SingleRequestController
import epicarchitect.breakbadhabits.foundation.controller.requireSelectedItems
import epicarchitect.breakbadhabits.foundation.viewmodel.ViewModel
import epicarchitect.breakbadhabits.logic.habits.creator.HabitWidgetCreator
import epicarchitect.breakbadhabits.logic.habits.provider.HabitProvider
import kotlinx.coroutines.flow.map

class HabitAppWidgetCreationViewModel(
    habitProvider: HabitProvider,
    habitWidgetCreator: HabitWidgetCreator,
    widgetSystemId: Int
) : ViewModel() {

    val titleInputController = InputController(
        coroutineScope = viewModelScope,
        initialInput = ""
    )

    val habitsSelectionController = MultiSelectionController(
        coroutineScope = viewModelScope,
        itemsFlow = habitProvider.habitsFlow()
    )

    val creationController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            habitWidgetCreator.createAppWidget(
                title = titleInputController.state.value.input,
                systemId = widgetSystemId,
                habitIds = habitsSelectionController.requireSelectedItems().map { it.id }
            )
        },
        isAllowedFlow = habitsSelectionController.state.map {
            it is MultiSelectionController.State.Loaded && it.selectedItems.isNotEmpty()
        }
    )
}