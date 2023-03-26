package breakbadhabits.app.presentation.habits

import androidx.lifecycle.viewModelScope
import breakbadhabits.app.logic.habits.entity.Habit
import breakbadhabits.app.logic.habits.HabitDeleter
import breakbadhabits.app.logic.habits.HabitProvider
import breakbadhabits.app.logic.habits.HabitUpdater
import breakbadhabits.app.logic.habits.CorrectHabitNewName
import breakbadhabits.app.logic.habits.HabitNewNameValidator
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
    habitId: Habit.Id,
) : ViewModel() {

    private val initialHabit = MutableStateFlow<Habit?>(null)

    val habitIconSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        items = localIconProvider.getIcons().map(Habit::Icon),
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

