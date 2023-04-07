package breakbadhabits.app.logic.habits.provider

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.database.asFlowOfList
import breakbadhabits.app.database.asFlowOfOneOrNull
import breakbadhabits.app.logic.habits.model.HabitWidget
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import breakbadhabits.app.database.HabitWidget as DatabaseHabitWidget

class HabitWidgetProvider(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val appDatabase: AppDatabase
) {
    fun provideFlowById(
        id: Int
    ) = appDatabase.habitWidgetQueries
        .selectById(id)
        .asFlowOfOneOrNull(coroutineDispatchers, ::asHabitWidget)

    fun provideFlowBySystemId(
        systemId: Int
    ) = appDatabase.habitWidgetQueries
        .selectBySystemId(systemId)
        .asFlowOfOneOrNull(coroutineDispatchers, ::asHabitWidget)

    fun provideAllFlow() = appDatabase.habitWidgetQueries
        .selectAll()
        .asFlowOfList(coroutineDispatchers, ::asHabitWidget)

    private fun asHabitWidget(value: DatabaseHabitWidget) = with(value) {
        HabitWidget(
            id = id,
            title = title,
            systemId = systemId,
            habitIds = habitIds
        )
    }
}