package breakbadhabits.android.app.feature

import breakbadhabits.android.app.repository.HabitsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class HabitEventCreationFeature(
    private val coroutineScope: CoroutineScope,
    private val habitsRepository: HabitsRepository
) {

    fun startCreation(
        habitId: Int,
        timeInMillis: Long,
        comment: String?
    ) {
        coroutineScope.launch {
            habitsRepository.createHabitEvent(
                habitId, timeInMillis, comment
            )
        }
    }

}