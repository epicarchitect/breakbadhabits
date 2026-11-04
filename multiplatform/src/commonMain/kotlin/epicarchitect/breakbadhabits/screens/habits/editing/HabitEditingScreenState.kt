package epicarchitect.breakbadhabits.screens.habits.editing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.habits.HabitNewNameError
import epicarchitect.breakbadhabits.infrastructure.AppDatabase
import kotlin.uuid.Uuid

class HabitEditingScreenState(
    val initialHabit: Habit?
) {
    var habitName by mutableStateOf(initialHabit?.name.orEmpty())
    var habitNameError by mutableStateOf<HabitNewNameError?>(null)
    var selectedIconId by mutableIntStateOf(initialHabit?.iconId ?: 0)
}

@Composable
fun rememberHabitEditingScreenState(
    habitId: Uuid? = null
): HabitEditingScreenState? {
    if (habitId == null) {
        return remember {
            HabitEditingScreenState(initialHabit = null)
        }
    } else {
        val habitsDao = AppDatabase.habitsDao
        val habitState = remember {
            habitsDao.habitById(habitId)
        }.collectAsState(null)
        val habit = habitState.value ?: return null
        return remember {
            HabitEditingScreenState(initialHabit = habit)
        }
    }
}