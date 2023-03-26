package breakbadhabits.app.presentation.habits

import androidx.lifecycle.viewModelScope
import breakbadhabits.app.logic.habits.entity.HabitAppWidgetConfig
import breakbadhabits.app.logic.habits.HabitProvider
import breakbadhabits.app.logic.habits.appWidgetConfig.HabitAppWidgetConfigDeleter
import breakbadhabits.app.logic.habits.appWidgetConfig.HabitAppWidgetConfigProvider
import breakbadhabits.app.logic.habits.appWidgetConfig.HabitAppWidgetConfigUpdater
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
    habitAppWidgetConfigProvider: HabitAppWidgetConfigProvider,
    habitAppWidgetConfigUpdater: HabitAppWidgetConfigUpdater,
    habitAppWidgetConfigDeleter: HabitAppWidgetConfigDeleter,
    id: HabitAppWidgetConfig.Id
) : ViewModel() {

    private val initialConfig = MutableStateFlow<HabitAppWidgetConfig?>(null)

    val titleInputController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = HabitAppWidgetConfig.Title(""),
        validation = { null }
    )

    val habitsSelectionController = MultiSelectionController(
        coroutineScope = viewModelScope,
        itemsFlow = habitProvider.habitsFlow()
    )

    val updatingController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            habitAppWidgetConfigUpdater.updateAppWidget(
                id = id,
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
            }.keys.map { it.id }.sortedBy { it.value }

            val isChanged = initial?.title != title.input
                    || initial.habitIds.sortedBy { it.value } != habitIdsInput

            isChanged && habitIdsInput.isNotEmpty()
        }
    )

    val deletionController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            habitAppWidgetConfigDeleter.deleteById(id)
        }
    )

    init {
        viewModelScope.launch {
            val config = checkNotNull(habitAppWidgetConfigProvider.provideFlowById(id).first())
            val habits = habitProvider.habitsFlow().first()
            initialConfig.value = config
            titleInputController.changeInput(config.title)
            habitsSelectionController.checkList(
                habits.filter { config.habitIds.contains(it.id) }
            )
        }
    }
}