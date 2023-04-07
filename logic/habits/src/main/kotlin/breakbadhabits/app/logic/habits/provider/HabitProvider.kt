package breakbadhabits.app.logic.habits.provider

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.database.asFlowOfList
import breakbadhabits.app.database.asFlowOfOneOrNull
import breakbadhabits.app.logic.habits.model.Habit
import breakbadhabits.app.logic.icons.LocalIconProvider
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import breakbadhabits.app.database.Habit as DababaseHabit

class HabitProvider(
    private val appDatabase: AppDatabase,
    private val coroutineDispatchers: CoroutineDispatchers,
    private val localIconProvider: LocalIconProvider
) {

    fun habitsFlow() = appDatabase.habitQueries
        .selectAll()
        .asFlowOfList(coroutineDispatchers, ::toHabit)

    fun habitFlow(id: Int) = appDatabase.habitQueries
        .selectById(id)
        .asFlowOfOneOrNull(coroutineDispatchers, ::toHabit)

    private fun toHabit(value: DababaseHabit) = with(value) {
        Habit(
            id = id,
            name = name,
            icon = localIconProvider.getIcon(iconId)
        )
    }
}