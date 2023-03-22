package breakbadhabits.app.presentation.habits

import androidx.lifecycle.viewModelScope
import breakbadhabits.app.entity.HabitAppWidgetConfig
import breakbadhabits.app.logic.habits.HabitProvider
import breakbadhabits.app.logic.habits.appWidgetConfig.HabitAppWidgetConfigCreator
import breakbadhabits.foundation.controller.RequestController
import breakbadhabits.foundation.controller.ValidatedInputController
import breakbadhabits.foundation.viewmodel.ViewModel
import kotlinx.coroutines.flow.first

class HabitAppWidgetCreationViewModel(
    private val habitProvider: HabitProvider,
    private val habitAppWidgetConfigCreator: HabitAppWidgetConfigCreator,
    private val appWidgetId: HabitAppWidgetConfig.AppWidgetId
) : ViewModel() {

    val titleInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = HabitAppWidgetConfig.Title(""),
        validation = { null }
    )

    val creationController = RequestController(
        coroutineScope = viewModelScope,
        request = {
            habitAppWidgetConfigCreator.createAppWidget(
                title = titleInputController.state.value.input,
                appWidgetId = appWidgetId,
                habitIds = habitProvider.habitsFlow().first().map { it.id }
            )
        }
    )
}