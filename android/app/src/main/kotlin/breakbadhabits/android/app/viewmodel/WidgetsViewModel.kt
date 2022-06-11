package breakbadhabits.android.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import breakbadhabits.android.app.coroutines.flow.mapItems
import breakbadhabits.android.app.data.HabitData
import breakbadhabits.android.app.data.HabitsAppWidgetData
import breakbadhabits.android.app.repository.AppWidgetsRepository
import breakbadhabits.android.app.repository.HabitsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WidgetsViewModel(
    private val habitsRepository: HabitsRepository,
    private val appWidgetsRepository: AppWidgetsRepository
) : ViewModel() {

    val widgets = appWidgetsRepository.habitsAppWidgetConfigListFlow().mapItems {
        WidgetItem(
            it,
            habitsRepository.habitListByIds(it.habitIds)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun deleteWidget(id: Int) = viewModelScope.launch {
        appWidgetsRepository.deleteHabitsAppWidgetConfigById(id)
    }

    class WidgetItem(
        val widgetConfig: HabitsAppWidgetData,
        val habits: List<HabitData>
    )
}