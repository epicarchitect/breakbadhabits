package breakbadhabits.app.presentation.habits

import androidx.lifecycle.viewModelScope
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.logic.habits.deleter.HabitDeleter
import breakbadhabits.app.logic.habits.provider.HabitIconProvider
import breakbadhabits.app.logic.habits.provider.HabitProvider
import breakbadhabits.app.logic.habits.updater.HabitUpdater
import breakbadhabits.app.logic.habits.validator.CorrectHabitNewName
import breakbadhabits.app.logic.habits.validator.HabitNewNameValidator
import breakbadhabits.foundation.controller.RequestController
import breakbadhabits.foundation.controller.SingleSelectionController
import breakbadhabits.foundation.controller.ValidatedInputController
import breakbadhabits.foundation.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class HabitUpdatingViewModel(
    habitProvider: HabitProvider,
    habitUpdater: HabitUpdater,
    habitDeleter: HabitDeleter,
    habitNewNameValidator: HabitNewNameValidator,
    habitIconProvider: HabitIconProvider,
    habitId: Habit.Id,
) : ViewModel() {

    private val initialHabit = MutableStateFlow<Habit?>(null)

    val habitIconSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        items = habitIconProvider.provide(),
        default = List<Habit.Icon>::first
    )

    val habitNameController = ValidatedInputController(
        coroutineScope = viewModelScope,
        initialInput = Habit.Name(""),
        validation = {
            habitNewNameValidator.validate(
                data = this,
                initial = initialHabit.value?.name
            )
        }
    )

    val updatingController = RequestController(
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

    val deletionController = RequestController(
        coroutineScope = viewModelScope,
        request = {
            habitDeleter.deleteHabit(habitId)
        }
    )

    init {
        viewModelScope.launch {
            val habit = checkNotNull(habitProvider.getHabit(habitId))
            initialHabit.value = habit
            habitNameController.changeInput(habit.name)
            habitIconSelectionController.select(habit.icon)
        }
    }
}

