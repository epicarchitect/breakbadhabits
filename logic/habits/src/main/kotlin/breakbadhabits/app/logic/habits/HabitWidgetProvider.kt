package breakbadhabits.app.logic.habits

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.logic.habits.model.HabitWidget
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import breakbadhabits.foundation.coroutines.flow.mapItems
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import breakbadhabits.app.database.HabitWidget as DatabaseHabitWidget

class HabitWidgetProvider(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val appDatabase: AppDatabase
) {
    fun provideFlowById(
        id: Int
    ) = appDatabase.habitWidgetQueries.selectById(id)
        .asFlow()
        .mapToOneOrNull(coroutineDispatchers.io)
        .map {
            it?.toEntity()
        }.flowOn(coroutineDispatchers.default)

    fun provideFlowBySystemId(
        systemId: Int
    ) = appDatabase.habitWidgetQueries.selectBySystemId(systemId)
        .asFlow()
        .mapToOneOrNull(coroutineDispatchers.io)
        .map {
            it?.toEntity()
        }.flowOn(coroutineDispatchers.default)

    fun provideAllFlow() = appDatabase.habitWidgetQueries.selectAll()
        .asFlow()
        .mapToList(coroutineDispatchers.io)
        .mapItems {
            it.toEntity()
        }.flowOn(coroutineDispatchers.io)

    private fun DatabaseHabitWidget.toEntity() = HabitWidget(
        id = id,
        title = title,
        systemId = systemId,
        habitIds = habitIds
    )
}