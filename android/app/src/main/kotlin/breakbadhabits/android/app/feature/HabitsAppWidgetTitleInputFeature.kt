package breakbadhabits.android.app.feature

import breakbadhabits.android.app.repository.AppWidgetsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HabitsAppWidgetTitleInputFeature(
    coroutineScope: CoroutineScope,
    appWidgetsRepository: AppWidgetsRepository,
    configId: Int?
) {

    var initialInput: String? = null
        private set

    private val mutableInput = MutableStateFlow<String?>(null)
    val input = mutableInput.asStateFlow()

    init {
        if (configId != null) {
            coroutineScope.launch {
                appWidgetsRepository.habitsAppWidgetConfigByIdFlow(configId).first()?.title?.let {
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