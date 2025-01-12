package epicarchitect.breakbadhabits.screens.habits.editing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.environment.LocalAppEnvironment
import epicarchitect.breakbadhabits.habits.HabitNewNameError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

class HabitEditingScreenState(
    val initialHabit: Habit?
) {
    var habitName by mutableStateOf(initialHabit?.name.orEmpty())
    var habitNameError by mutableStateOf<HabitNewNameError?>(null)
    var selectedIconId by mutableIntStateOf(initialHabit?.iconId ?: 0)
}

@Composable
fun rememberHabitEditingScreenState(
    habitId: Int? = null
): HabitEditingScreenState? {
    if (habitId == null) {
        return remember {
            HabitEditingScreenState(initialHabit = null)
        }
    } else {
        val environment = LocalAppEnvironment.current
        val habitQueries = environment.database.habitQueries
        val habitState = remember {
            habitQueries.habitById(habitId)
                .asFlow()
                .mapToOneOrNull(Dispatchers.IO)
        }.collectAsState(null)
        val habit = habitState.value ?: return null
        return remember {
            HabitEditingScreenState(initialHabit = habit)
        }
    }
}