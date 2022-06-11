package breakbadhabits.android.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import breakbadhabits.android.app.repository.AppWidgetsRepository
import breakbadhabits.android.app.repository.HabitsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HabitsAppWidgetConfigEditingViewModel(
    habitsRepository: HabitsRepository,
    private val appWidgetsRepository: AppWidgetsRepository,
    private val configId: Int
) : ViewModel() {

    private val initialConfigFlow = appWidgetsRepository.habitsAppWidgetConfigByIdFlow(configId)
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
        savingStateFlow,
        initialConfigFlow,
        titleStateFlow
    ) { checked, saving, initial, title ->
        checked.isNotEmpty()
                && saving is SavingState.NotExecuted
                && initial?.let {
            it.title != title || !habitsIdsEquals(
                checked.toList(),
                it.habitIds
            )
        } ?: false
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    fun habitsFlow() = habitsFlow

    fun titleStateFlow() = titleStateFlow.asStateFlow()

    fun savingStateFlow() = savingStateFlow.asStateFlow()

    fun savingAllowedStateFlow() = savingAllowedStateFlow

    init {
        initialConfigFlow.onEach {
            titleStateFlow.value = it?.title ?: ""
            checkedHabitIds.value = it?.habitIds?.toSet() ?: emptySet()
        }.launchIn(viewModelScope)
    }

    fun setCheckedHabit(habitId: Int, checked: Boolean) = viewModelScope.launch {
        if (checked) {
            checkedHabitIds.value = checkedHabitIds.value + habitId
        } else {
            checkedHabitIds.value = checkedHabitIds.value - habitId
        }
    }

    fun save() = viewModelScope.launch {
        require(savingAllowedStateFlow.value) { "Creation must be allowed!" }

        savingStateFlow.value = SavingState.Executing()
        appWidgetsRepository.updateHabitsAppWidget(
            configId,
            titleStateFlow.value,
            checkedHabitIds.value.toList()
        )
        savingStateFlow.value = SavingState.Executed()
    }

    fun updateTitle(title: String) {
        titleStateFlow.value = title
    }

    private fun habitsIdsEquals(habitIds1: List<Int>, habitIds2: List<Int>) = habitIds1.sorted() == habitIds2.sorted()

    fun deleteWidget() = viewModelScope.launch {
        appWidgetsRepository.deleteHabitsAppWidgetConfigById(configId)
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