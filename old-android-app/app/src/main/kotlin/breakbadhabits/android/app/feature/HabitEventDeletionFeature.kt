package breakbadhabits.android.app.feature

import breakbadhabits.android.app.repository.DefaultHabitsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class HabitEventDeletionFeature(
    private val coroutineScope: CoroutineScope,
    private val habitsRepository: DefaultHabitsRepository
) {

    fun startDeletion(habitEventId: Int) {
        coroutineScope.launch {
            habitsRepository.deleteHabitEventById(habitEventId)
        }
    }

}