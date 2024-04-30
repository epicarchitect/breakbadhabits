package epicarchitect.breakbadhabits.logic.habits.provider

import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineDispatchers
import epicarchitect.breakbadhabits.foundation.sqldelight.asFlowOfList
import epicarchitect.breakbadhabits.foundation.sqldelight.asFlowOfOneOrNull
import epicarchitect.breakbadhabits.logic.habits.model.HabitWidget
import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase
import epicarchitect.breakbadhabits.sqldelight.main.HabitWidget as DatabaseHabitWidget

class HabitWidgetMapperMapp(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val mainDatabase: MainDatabase
) {
    fun provideFlowById(
        id: Int
    ) = mainDatabase.habitWidgetQueries
        .selectById(id)
        .asFlowOfOneOrNull(coroutineDispatchers, ::mapToHabitWidget)

    fun provideFlowBySystemId(
        systemId: Int
    ) = mainDatabase.habitWidgetQueries
        .selectBySystemId(systemId)
        .asFlowOfOneOrNull(coroutineDispatchers, ::mapToHabitWidget)

    fun provideAllFlow() = mainDatabase.habitWidgetQueries
        .selectAll()
        .asFlowOfList(coroutineDispatchers, ::mapToHabitWidget)

    private fun mapToHabitWidget(value: DatabaseHabitWidget) = with(value) {
        HabitWidget(
            id = id,
            title = title,
            systemId = systemId,
            habitIds = habitIds
        )
    }
}