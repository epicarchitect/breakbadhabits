package breakbadhabits.android.app.feature

import breakbadhabits.android.app.repository.AppWidgetsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HabitsAppWidgetHabitIdsFeature(
    coroutineScope: CoroutineScope,
    appWidgetsRepository: AppWidgetsRepository,
    configId: Int
) {

    val state = appWidgetsRepository.habitsAppWidgetConfigByIdFlow(configId).map {
        it?.habitIds
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), null)

}