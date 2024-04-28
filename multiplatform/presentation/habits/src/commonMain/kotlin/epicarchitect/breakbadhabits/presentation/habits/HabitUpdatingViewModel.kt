package epicarchitect.breakbadhabits.presentation.habits

import epicarchitect.breakbadhabits.foundation.controller.SingleRequestController
import epicarchitect.breakbadhabits.foundation.controller.SingleSelectionController
import epicarchitect.breakbadhabits.foundation.controller.ValidatedInputController
import epicarchitect.breakbadhabits.foundation.controller.requireSelectedItem
import epicarchitect.breakbadhabits.foundation.controller.validateAndRequire
import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineScopeOwner
import epicarchitect.breakbadhabits.foundation.coroutines.flow.firstNotNull
import epicarchitect.breakbadhabits.foundation.icons.Icons
import epicarchitect.breakbadhabits.logic.habits.deleter.HabitDeleter
import epicarchitect.breakbadhabits.logic.habits.provider.HabitProvider
import epicarchitect.breakbadhabits.logic.habits.updater.HabitUpdater
import epicarchitect.breakbadhabits.logic.habits.validator.CorrectHabitNewName
import epicarchitect.breakbadhabits.logic.habits.validator.HabitNewNameValidator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take

class HabitUpdatingViewModel(
    override val coroutineScope: CoroutineScope,
    habitProvider: HabitProvider,
    habitUpdater: HabitUpdater,
    habitDeleter: HabitDeleter,
    habitNewNameValidator: HabitNewNameValidator,
    icons: Icons,
    habitId: Int
) : CoroutineScopeOwner {

    private val initialHabit = habitProvider.habitFlow(habitId).take(1).stateIn(
        scope = coroutineScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    val habitIconSelectionController = SingleSelectionController(
        itemsFlow = icons.iconsFlow(),
        default = { initialHabit.firstNotNull().icon }
    )

    val habitNameController = ValidatedInputController(
        initialInput = "",
        validation = {
            habitNewNameValidator.validate(
                data = this,
                initial = initialHabit.value?.name
            )
        }
    )

    val updatingController = SingleRequestController(
        request = {
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

    val deletionController = SingleRequestController {
        habitDeleter.deleteHabit(habitId)
    }
}