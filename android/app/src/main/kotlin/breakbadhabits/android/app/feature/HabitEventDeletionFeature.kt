package breakbadhabits.android.app.feature

import breakbadhabits.android.app.repository.HabitsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HabitEventDeletionFeature(
    private val coroutineScope: CoroutineScope,
    private val habitsRepository: HabitsRepository
) {

    fun startDeletion(habitEventId: Int) {
        coroutineScope.launch {
            habitsRepository.deleteHabitEventById(habitEventId)
        }
    }

}