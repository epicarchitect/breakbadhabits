package epicarchitect.breakbadhabits.presentation.habits

import epicarchitect.breakbadhabits.foundation.controller.InputController
import epicarchitect.breakbadhabits.foundation.controller.MultiSelectionController
import epicarchitect.breakbadhabits.foundation.controller.SingleRequestController
import epicarchitect.breakbadhabits.foundation.controller.requireSelectedItems
import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineScopeOwner
import epicarchitect.breakbadhabits.logic.habits.creator.HabitWidgetCreator
import epicarchitect.breakbadhabits.logic.habits.provider.HabitProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map

class HabitAppWidgetCreationViewModel(
    override val coroutineScope: CoroutineScope,
    habitProvider: HabitProvider,
    habitWidgetCreator: HabitWidgetCreator,
    widgetSystemId: Int
) : CoroutineScopeOwner {

    val titleInputController = InputController(initialInput = "")

    val habitsSelectionController = MultiSelectionController(habitProvider.habitsFlow())

    val creationController = SingleRequestController(
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