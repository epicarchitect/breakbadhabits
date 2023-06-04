package epicarchitect.breakbadhabits.logic.habits.provider

import epicarchitect.breakbadhabits.database.asFlowOfList
import epicarchitect.breakbadhabits.database.asFlowOfOneOrNull
import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineDispatchers
import epicarchitect.breakbadhabits.foundation.icons.IconProvider
import epicarchitect.breakbadhabits.foundation.icons.requireIcon
import epicarchitect.breakbadhabits.logic.habits.model.Habit
import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase
import epicarchitect.breakbadhabits.sqldelight.main.Habit as DatabaseHabit

class HabitProvider(
    private val mainDatabase: MainDatabase,
    private val coroutineDispatchers: CoroutineDispatchers,
    private val iconProvider: IconProvider
) {

    fun habitsFlow() = mainDatabase.habitQueries
        .selectAll()
        .asFlowOfList(coroutineDispatchers, ::toHabit)

    fun habitFlow(id: Int) = mainDatabase.habitQueries
        .selectById(id)
        .asFlowOfOneOrNull(coroutineDispatchers, ::toHabit)

    private suspend fun toHabit(value: DatabaseHabit) = with(value) {
        Habit(
            id = id,
            name = name,
            icon = iconProvider.requireIcon(iconId)
        )
    }
}