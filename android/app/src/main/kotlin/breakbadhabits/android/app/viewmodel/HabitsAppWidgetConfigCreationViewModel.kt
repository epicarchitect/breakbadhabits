package breakbadhabits.android.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import breakbadhabits.android.app.repository.AppWidgetsRepository
import breakbadhabits.android.app.repository.HabitsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HabitsAppWidgetConfigCreationViewModel(
    habitsRepository: HabitsRepository,
    private val appWidgetsRepository: AppWidgetsRepository,
    val appWidgetId: Int
) : ViewModel() {

    private val checkedHabitIds = MutableStateFlow<Set<Int>>(emptySet())
    private val habitsFlow = combine(
        habitsRepository.habitListFlow(),
        checkedHabitIds
    ) { habits, checked ->
        habits.map {
            Habit(
                it.id,
                it.name,
                checked.contains(it.id)
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val titleStateFlow = MutableStateFlow("")
    private val savingStateFlow = MutableStateFlow<SavingState>(SavingState.NotExecuted())
    private val savingAllowedStateFlow = combine(
        checkedHabitIds,
        savingStateFlow
    ) { checked, saving ->
        checked.isNotEmpty() && saving is SavingState.NotExecuted
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    fun habitsFlow() = habitsFlow

    fun titleStateFlow() = titleStateFlow.asStateFlow()

    fun savingStateFlow() = savingStateFlow.asStateFlow()

    fun savingAllowedStateFlow() = savingAllowedStateFlow

    private fun creationAllowed() = checkedHabitIds.value.isNotEmpty()
            && savingStateFlow.value is SavingState.NotExecuted


    fun setCheckedHabit(habitId: Int, checked: Boolean) {
        if (checked) {
            checkedHabitIds.value = checkedHabitIds.value + habitId
        } else {
            checkedHabitIds.value = checkedHabitIds.value - habitId
        }
    }

    fun save() = viewModelScope.launch {
        require(savingAllowedStateFlow.value) { "Creation must be allowed!" }

        savingStateFlow.value = SavingState.Executing()
        appWidgetsRepository.createHabitsAppWidgetConfig(
            titleStateFlow.value,
            appWidgetId,
            checkedHabitIds.value.toList()
        )
        savingStateFlow.value = SavingState.Executed()
    }

    fun updateTitle(title: String) {
        titleStateFlow.value = title
    }

    data class Habit(
        val id: Int,
        val name: String,
        val isChecked: Boolean
    )

    sealed class SavingState {
        class NotExecuted : SavingState()
        class Executing : SavingState()
        class Executed : SavingState()
    }
}