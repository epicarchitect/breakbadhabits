package breakbadhabits.android.app.feature

import breakbadhabits.android.app.repository.AppWidgetsRepository
import breakbadhabits.coroutines.ext.flow.mapItems
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class HabitsAppWidgetConfigIdsFeature(
    coroutineScope: CoroutineScope,
    appWidgetsRepository: AppWidgetsRepository
) {

    val state = appWidgetsRepository.habitsAppWidgetConfigListFlow().mapItems {
        it.id
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), emptyList())

}