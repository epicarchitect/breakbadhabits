package breakbadhabits.android.app.feature

import breakbadhabits.android.app.repository.AppWidgetsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class HabitsAppWidgetCreationFeature(
    private val coroutineScope: CoroutineScope,
    private val appWidgetsRepository: AppWidgetsRepository
) {

    fun startCreation(
        title: String?,
        appWidgetId: Int,
        habitIds: Set<Int>
    ) {
        coroutineScope.launch {
            appWidgetsRepository.createHabitsAppWidgetConfig(
                title = title ?: "",
                appWidgetId = appWidgetId,
                habitIds = habitIds
            )
        }
    }
}