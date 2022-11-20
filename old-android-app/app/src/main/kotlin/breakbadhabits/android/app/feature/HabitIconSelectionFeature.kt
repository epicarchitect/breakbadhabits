package breakbadhabits.android.app.feature

import breakbadhabits.android.app.repository.DefaultHabitsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HabitIconSelectionFeature(
    coroutineScope: CoroutineScope,
    private val habitsRepository: DefaultHabitsRepository,
    private val habitId: Int?
) {

    var initialValue: Int? = null

    private val mutableSelection = MutableStateFlow(0)
    val selection = mutableSelection.asStateFlow()

    init {
        if (habitId != null) {
            coroutineScope.launch {
                habitsRepository.habitByIdFlow(habitId).first()?.iconId?.let {
                    initialValue = it
                    mutableSelection.value = it
                }
            }
        }
    }

    fun changeSelection(value: Int) {
        mutableSelection.value = value
    }
}