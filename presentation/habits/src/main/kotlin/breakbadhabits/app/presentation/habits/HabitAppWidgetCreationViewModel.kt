package breakbadhabits.app.presentation.habits

import androidx.lifecycle.viewModelScope
import breakbadhabits.app.logic.habits.HabitProvider
import breakbadhabits.app.logic.habits.HabitWidgetCreator
import breakbadhabits.foundation.controller.MultiSelectionController
import breakbadhabits.foundation.controller.SingleRequestController
import breakbadhabits.foundation.controller.ValidatedInputController
import breakbadhabits.foundation.viewmodel.ViewModel
import kotlinx.coroutines.flow.map

class HabitAppWidgetCreationViewModel(
    habitProvider: HabitProvider,
    habitWidgetCreator: HabitWidgetCreator,
    widgetSystemId: Int
) : ViewModel() {

    val titleInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = "",
        validation = { null }
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
                habitIds = habitsSelectionController.state.value
                    .items.filter { it.value }
                    .keys.map { it.id }
            )
        },
        isAllowedFlow = habitsSelectionController.state.map {
            it.items.values.contains(true)
        }
    )
}