package breakbadhabits.android.app.feature

import breakbadhabits.android.app.repository.AppWidgetsRepository
import breakbadhabits.android.app.repository.HabitsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HabitsAppWidgetHabitIdsSelectionFeature(
    coroutineScope: CoroutineScope,
    habitsRepository: HabitsRepository,
    appWidgetsRepository: AppWidgetsRepository,
    configId: Int?
) {

    var initialValue = emptySet<Int>()

    private val selectedHabitIds = MutableStateFlow(emptySet<Int>())

    val selection = combine(
        habitsRepository.habitListFlow(),
        selectedHabitIds
    ) { habits, selectedIds ->
        habits.associate {
            it.id to selectedIds.contains(it.id)
        }
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), emptyMap())

    init {
        if (configId != null) {
            coroutineScope.launch {
                appWidgetsRepository
                    .habitsAppWidgetConfigByIdFlow(configId)
                    .first()
                    ?.habitIds
                    ?.let {
                        initialValue = it
                        selectedHabitIds.value = it
                    }
            }
        }
    }

    fun setChecked(habitId: Int, isChecked: Boolean) {
        selectedHabitIds.update {
            if (isChecked) it + habitId
            else it - habitId
        }
    }
}