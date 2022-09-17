package breakbadhabits.android.app.feature

import breakbadhabits.android.app.repository.AppWidgetsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class HabitsAppWidgetDeletionFeature(
    private val coroutineScope: CoroutineScope,
    private val appWidgetsRepository: AppWidgetsRepository
) {

    fun startDeletion(configId: Int) {
        coroutineScope.launch {
            appWidgetsRepository.deleteHabitsAppWidgetConfigById(
                id = configId
            )
        }
    }
}