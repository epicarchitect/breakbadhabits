package breakbadhabits.android.app.feature

import breakbadhabits.android.app.repository.DefaultHabitsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HabitEventCommentInputFeature(
    coroutineScope: CoroutineScope,
    habitsRepository: DefaultHabitsRepository,
    habitEventId: Int?
) {

    var initialInput: String? = null
        private set

    private val mutableInput = MutableStateFlow<String?>(null)
    val input = mutableInput.asStateFlow()

    init {
        if (habitEventId != null) {
            coroutineScope.launch {
                habitsRepository.habitEventByIdFlow(habitEventId).first()?.comment?.let {
                    initialInput = it
                    mutableInput.value = it
                }
            }
        }
    }

    fun changeInput(value: String?) {
        mutableInput.value = value
    }
}