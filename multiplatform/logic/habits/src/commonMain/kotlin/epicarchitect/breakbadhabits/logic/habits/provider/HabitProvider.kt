package epicarchitect.breakbadhabits.logic.habits.provider

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineDispatchers
import epicarchitect.breakbadhabits.foundation.coroutines.flow.flatMapOrNullLatest
import epicarchitect.breakbadhabits.foundation.icons.Icon
import epicarchitect.breakbadhabits.foundation.icons.Icons
import epicarchitect.breakbadhabits.logic.habits.model.Habit
import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import epicarchitect.breakbadhabits.sqldelight.main.Habit as DatabaseHabit

class HabitProvider(
    private val mainDatabase: MainDatabase,
    private val coroutineDispatchers: CoroutineDispatchers,
    private val icons: Icons
) {

    fun habitsFlow() = combine(
        mainDatabase.habitQueries
            .selectAll()
            .asFlow()
            .mapToList(coroutineDispatchers.io),
        icons.iconsFlow()
    ) { habits, icons ->
        habits.map { habit ->
            mapToHabit(
                databaseHabit = habit,
                icon = icons.first { it.id == habit.iconId }
            )
        }
    }

    fun habitFlow(id: Int) = mainDatabase.habitQueries
        .selectById(id)
        .asFlow()
        .mapToOneOrNull(coroutineDispatchers.io)
        .flatMapOrNullLatest { habit ->
            icons.iconFlow(habit.iconId).map { icon ->
                mapToHabit(
                    databaseHabit = habit,
                    icon = checkNotNull(icon)
                )
            }
        }

    private fun mapToHabit(
        databaseHabit: DatabaseHabit,
        icon: Icon
    ) = with(databaseHabit) {
        Habit(
            id = id,
            name = name,
            icon = icon
        )
    }
}