package breakbadhabits.android.app

import androidx.compose.runtime.Composable
import breakbadhabits.android.app.utils.get
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


fun createHabitIdsFeature() = HabitIdsFeature(
    coroutineScope = CoroutineScope(Dispatchers.Default),
    habitsRepository = get()
)

fun createHabitIconIdFeature(habitId: Int) = HabitIconIdFeature(
    coroutineScope = CoroutineScope(Dispatchers.Default),
    habitsRepository = get(),
    habitId = habitId
)

fun createHabitAbstinenceTimeFeature(habitId: Int) = HabitAbstinenceTimeFeature(
    coroutineScope = CoroutineScope(Dispatchers.Default),
    habitsRepository = get(),
    habitId = habitId
)

@Composable
fun createHabitNameFeature(habitId: Int) = HabitNameFeature(
    coroutineScope = CoroutineScope(Dispatchers.Default),
    habitsRepository = get(),
    habitId = habitId
)