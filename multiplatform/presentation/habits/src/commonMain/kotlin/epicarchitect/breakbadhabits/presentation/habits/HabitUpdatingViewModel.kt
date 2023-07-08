package epicarchitect.breakbadhabits.presentation.habits

import epicarchitect.breakbadhabits.foundation.controller.SingleRequestController
import epicarchitect.breakbadhabits.foundation.controller.SingleSelectionController
import epicarchitect.breakbadhabits.foundation.controller.ValidatedInputController
import epicarchitect.breakbadhabits.foundation.controller.requireSelectedItem
import epicarchitect.breakbadhabits.foundation.controller.validateAndRequire
import epicarchitect.breakbadhabits.foundation.coroutines.flow.firstNotNull
import epicarchitect.breakbadhabits.foundation.icons.IconProvider
import epicarchitect.breakbadhabits.foundation.viewmodel.ViewModel
import epicarchitect.breakbadhabits.logic.habits.deleter.HabitDeleter
import epicarchitect.breakbadhabits.logic.habits.provider.HabitProvider
import epicarchitect.breakbadhabits.logic.habits.updater.HabitUpdater
import epicarchitect.breakbadhabits.logic.habits.validator.CorrectHabitNewName
import epicarchitect.breakbadhabits.logic.habits.validator.HabitNewNameValidator
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class HabitUpdatingViewModel(
    habitProvider: HabitProvider,
    habitUpdater: HabitUpdater,
    habitDeleter: HabitDeleter,
    habitNewNameValidator: HabitNewNameValidator,
    iconProvider: IconProvider,
    habitId: Int
) : ViewModel() {

    private val initialHabit = habitProvider.habitFlow(habitId).take(1).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    val habitIconSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        itemsFlow = iconProvider.iconsFlow(),
        default = { initialHabit.firstNotNull().icon }
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
            viewModelScope.launch {
            }.isCancelled
            habitUpdater.updateHabit(
                habitId = habitId,
                habitName = habitNameController.validateAndRequire(),
                icon = habitIconSelectionController.requireSelectedItem()
            )
        },
        isAllowedFlow = combine(
            habitNameController.state,
            habitIconSelectionController.state,
            initialHabit
        ) { nameState, iconState, initialHabit ->
            if (initialHabit == null ||
                iconState !is SingleSelectionController.State.Loaded
            ) {
                return@combine false
            }

            val isChanged = initialHabit.name != nameState.input ||
                initialHabit.icon != iconState.selectedItem

            isChanged && nameState.validationResult.let {
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
}