package breakbadhabits.android.app.feature

import breakbadhabits.android.app.repository.HabitsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class HabitEventUpdatingFeature(
    private val coroutineScope: CoroutineScope,
    private val habitsRepository: HabitsRepository
) {
    fun startUpdating(
        habitEventId: Int,
        timeInMillis: Long,
        comment: String?
    ) {
        coroutineScope.launch {
            habitsRepository.updateHabitEvent(
                habitEventId,
                timeInMillis,
                comment
            )
        }
    }
}