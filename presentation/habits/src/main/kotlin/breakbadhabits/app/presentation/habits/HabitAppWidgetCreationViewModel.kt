package breakbadhabits.app.presentation.habits

import androidx.lifecycle.viewModelScope
import breakbadhabits.app.logic.habits.HabitProvider
import breakbadhabits.app.logic.habits.appWidgetConfig.HabitAppWidgetConfigCreator
import breakbadhabits.app.logic.habits.entity.HabitAppWidgetConfig
import breakbadhabits.foundation.controller.MultiSelectionController
import breakbadhabits.foundation.controller.SingleRequestController
import breakbadhabits.foundation.controller.ValidatedInputController
import breakbadhabits.foundation.viewmodel.ViewModel
import kotlinx.coroutines.flow.map

class HabitAppWidgetCreationViewModel(
    habitProvider: HabitProvider,
    habitAppWidgetConfigCreator: HabitAppWidgetConfigCreator,
    appWidgetId: HabitAppWidgetConfig.AppWidgetId
) : ViewModel() {

    val titleInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = HabitAppWidgetConfig.Title(""),
        validation = { null }
    )

    val habitsSelectionController = MultiSelectionController(
        coroutineScope = viewModelScope,
        itemsFlow = habitProvider.habitsFlow()
    )

    val creationController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            habitAppWidgetConfigCreator.createAppWidget(
                title = titleInputController.state.value.input,
                appWidgetId = appWidgetId,
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