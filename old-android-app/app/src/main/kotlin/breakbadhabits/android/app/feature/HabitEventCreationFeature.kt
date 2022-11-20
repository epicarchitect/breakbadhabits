package breakbadhabits.android.app.feature

import breakbadhabits.android.app.repository.DefaultHabitsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class HabitEventCreationFeature(
    private val coroutineScope: CoroutineScope,
    private val habitsRepository: DefaultHabitsRepository
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