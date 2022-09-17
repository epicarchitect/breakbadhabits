package breakbadhabits.android.app.feature

import breakbadhabits.android.app.repository.AppWidgetsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class HabitsAppWidgetUpdatingFeature(
    private val coroutineScope: CoroutineScope,
    private val appWidgetsRepository: AppWidgetsRepository
) {

    fun startUpdating(
        configId: Int,
        title: String?,
        habitIds: Set<Int>
    ) {
        coroutineScope.launch {
            appWidgetsRepository.updateHabitsAppWidget(
                id = configId,
                title = title ?: "",
                habitIds = habitIds
            )
        }
    }
}