package breakbadhabits.app.presentation.habits

import androidx.lifecycle.viewModelScope
import breakbadhabits.app.logic.habits.validator.CorrectHabitNewName
import breakbadhabits.app.logic.habits.deleter.HabitDeleter
import breakbadhabits.app.logic.habits.validator.HabitNewNameValidator
import breakbadhabits.app.logic.habits.provider.HabitProvider
import breakbadhabits.app.logic.habits.updater.HabitUpdater
import breakbadhabits.app.logic.habits.model.Habit
import breakbadhabits.app.logic.icons.LocalIcon
import breakbadhabits.app.logic.icons.LocalIconProvider
import breakbadhabits.foundation.controller.SingleRequestController
import breakbadhabits.foundation.controller.SingleSelectionController
import breakbadhabits.foundation.controller.ValidatedInputController
import breakbadhabits.foundation.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HabitUpdatingViewModel(
    habitProvider: HabitProvider,
    habitUpdater: HabitUpdater,
    habitDeleter: HabitDeleter,
    habitNewNameValidator: HabitNewNameValidator,
    localIconProvider: LocalIconProvider,
    habitId: Int
) : ViewModel() {

    private val initialHabit = MutableStateFlow<Habit?>(null)

    val habitIconSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        items = localIconProvider.getIcons(),
        default = List<LocalIcon>::first
    )

    val habitNameController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = "",
        validation = {
            habitNewNameValidator.validate(
                data = this,
                initial = initialHabit.value?.name
            )
        }
    )

    val updatingController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            val habitIcon = habitIconSelectionController.state.value.selectedItem
            val habitName = habitNameController.validateAndAwait()
            require(habitName is CorrectHabitNewName)

            habitUpdater.updateHabit(
                habitId,
                habitName,
                habitIcon
            )
        },
        isAllowedFlow = combine(
            habitNameController.state,
            habitIconSelectionController.state,
            initialHabit
        ) { name, icon, initialHabit ->
            val isChanged = initialHabit?.name != name.input
                    || initialHabit.icon != icon.selectedItem

            isChanged && name.validationResult.let {
                it == null || it is CorrectHabitNewName
            }
        }
    )

    val deletionController = SingleRequestController(
        coroutineScope = viewModelScope,
        request = {
            habitDeleter.deleteHabit(habitId)
        }
    )

    init {
        viewModelScope.launch {
            val habit = checkNotNull(habitProvider.habitFlow(habitId).first())
            initialHabit.value = habit
            habitNameController.changeInput(habit.name)
            habitIconSelectionController.select(habit.icon)
        }
    }
}

