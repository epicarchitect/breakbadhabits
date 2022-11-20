package breakbadhabits.android.app.feature

import breakbadhabits.android.app.repository.AppWidgetsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HabitsAppWidgetTitleFeature(
    coroutineScope: CoroutineScope,
    appWidgetsRepository: AppWidgetsRepository,
    configId: Int
) {

    val state = appWidgetsRepository.habitsAppWidgetConfigByIdFlow(configId).map {
        it?.title
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), null)

}