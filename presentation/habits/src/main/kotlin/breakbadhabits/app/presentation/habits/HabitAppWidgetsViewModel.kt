package breakbadhabits.app.presentation.habits

import androidx.lifecycle.viewModelScope
import breakbadhabits.app.logic.habits.appWidgetConfig.HabitAppWidgetConfigProvider
import breakbadhabits.foundation.controller.LoadingController
import breakbadhabits.foundation.viewmodel.ViewModel

class HabitAppWidgetsViewModel(
    private val habitAppWidgetConfigProvider: HabitAppWidgetConfigProvider
) : ViewModel() {

    val widgetsLoadingController = LoadingController(
        coroutineScope = viewModelScope,
        flow = habitAppWidgetConfigProvider.provideAllFlow()
    )
}