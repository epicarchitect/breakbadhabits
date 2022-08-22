package breakbadhabits.android.app

import breakbadhabits.android.app.feature.HabitAbstinenceTimeFeature
import breakbadhabits.android.app.feature.HabitCreationFeature
import breakbadhabits.android.app.feature.HabitDeletionFeature
import breakbadhabits.android.app.feature.HabitIconIdFeature
import breakbadhabits.android.app.feature.HabitIconSelectionFeature
import breakbadhabits.android.app.feature.HabitIdsFeature
import breakbadhabits.android.app.feature.HabitNameFeature
import breakbadhabits.android.app.feature.HabitNameInputFeature
import breakbadhabits.android.app.feature.HabitUpdatingFeature
import breakbadhabits.android.app.feature.HabitsAppWidgetConfigIdsFeature
import breakbadhabits.android.app.feature.HabitsAppWidgetHabitIdsFeature
import breakbadhabits.android.app.feature.HabitsAppWidgetIdFeature
import breakbadhabits.android.app.feature.HabitsAppWidgetTitleFeature
import breakbadhabits.android.app.feature.LastTimeInputFeature
import breakbadhabits.android.app.utils.get
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


/**
 * HABITS
 */
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

fun createHabitNameFeature(habitId: Int) = HabitNameFeature(
    coroutineScope = CoroutineScope(Dispatchers.Default),
    habitsRepository = get(),
    habitId = habitId
)

fun createHabitNameInputFeature(habitId: Int? = null) = HabitNameInputFeature(
    coroutineScope = CoroutineScope(Dispatchers.Default),
    habitsRepository = get(),
    habitValidator = get(),
    habitId = habitId
)

fun createLastTimeInputFeature() = LastTimeInputFeature(
    coroutineScope = CoroutineScope(Dispatchers.Default),
    habitEventValidator = get()
)

fun createHabitIconSelectionFeature(habitId: Int? = null) = HabitIconSelectionFeature(
    coroutineScope = CoroutineScope(Dispatchers.Default),
    habitsRepository = get(),
    habitId = habitId
)

fun createHabitCreationFeature() = HabitCreationFeature(
    coroutineScope = CoroutineScope(Dispatchers.Default),
    habitsRepository = get()
)

fun createHabitDeletionFeature(habitId: Int) = HabitDeletionFeature(
    coroutineScope = CoroutineScope(Dispatchers.Default),
    habitsRepository = get(),
    habitId = habitId
)

fun createHabitUpdatingFeature(habitId: Int) = HabitUpdatingFeature(
    coroutineScope = CoroutineScope(Dispatchers.Default),
    habitsRepository = get(),
    habitId = habitId
)

/**
 * WIDGETS
 */
fun createHabitsAppWidgetConfigIdsFeature() = HabitsAppWidgetConfigIdsFeature(
    coroutineScope = CoroutineScope(Dispatchers.Default),
    appWidgetsRepository = get()
)

fun createHabitsAppWidgetTitleFeature(configId: Int) = HabitsAppWidgetTitleFeature(
    coroutineScope = CoroutineScope(Dispatchers.Default),
    appWidgetsRepository = get(),
    configId = configId
)

fun createHabitsAppWidgetHabitIdsFeature(configId: Int) = HabitsAppWidgetHabitIdsFeature(
    coroutineScope = CoroutineScope(Dispatchers.Default),
    appWidgetsRepository = get(),
    configId = configId
)

fun createHabitsAppWidgetIdFeature(configId: Int) = HabitsAppWidgetIdFeature(
    coroutineScope = CoroutineScope(Dispatchers.Default),
    appWidgetsRepository = get(),
    configId = configId
)