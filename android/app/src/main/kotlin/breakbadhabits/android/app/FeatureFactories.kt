package breakbadhabits.android.app

import breakbadhabits.android.app.feature.CurrentMonthHabitEventCountFeature
import breakbadhabits.android.app.feature.CurrentMonthHabitEventIdsFeature
import breakbadhabits.android.app.feature.CurrentMonthHabitEventTimesFeature
import breakbadhabits.android.app.feature.HabitAbstinenceTimeFeature
import breakbadhabits.android.app.feature.HabitAbstinenceTimesFeature
import breakbadhabits.android.app.feature.HabitAverageAbstinenceTimeFeature
import breakbadhabits.android.app.feature.HabitCreationFeature
import breakbadhabits.android.app.feature.HabitDeletionFeature
import breakbadhabits.android.app.feature.HabitEventCommentFeature
import breakbadhabits.android.app.feature.HabitEventCommentInputFeature
import breakbadhabits.android.app.feature.HabitEventCountFeature
import breakbadhabits.android.app.feature.HabitEventCreationFeature
import breakbadhabits.android.app.feature.HabitEventDeletionFeature
import breakbadhabits.android.app.feature.HabitEventHabitIdFeature
import breakbadhabits.android.app.feature.HabitEventIdsFeature
import breakbadhabits.android.app.feature.HabitEventTimeFeature
import breakbadhabits.android.app.feature.HabitEventTimesFeature
import breakbadhabits.android.app.feature.HabitIconIdFeature
import breakbadhabits.android.app.feature.HabitIconSelectionFeature
import breakbadhabits.android.app.feature.HabitIdsFeature
import breakbadhabits.android.app.feature.HabitMaxAbstinenceTimeFeature
import breakbadhabits.android.app.feature.HabitMinAbstinenceTimeFeature
import breakbadhabits.android.app.feature.HabitNameFeature
import breakbadhabits.android.app.feature.HabitNameInputFeature
import breakbadhabits.android.app.feature.HabitTimeSinceFirstEventFeature
import breakbadhabits.android.app.feature.HabitUpdatingFeature
import breakbadhabits.android.app.feature.HabitsAppWidgetConfigIdsFeature
import breakbadhabits.android.app.feature.HabitsAppWidgetHabitIdsFeature
import breakbadhabits.android.app.feature.HabitsAppWidgetIdFeature
import breakbadhabits.android.app.feature.HabitsAppWidgetTitleFeature
import breakbadhabits.android.app.feature.HabitEventTimeInputFeature
import breakbadhabits.android.app.feature.HabitEventUpdatingFeature
import breakbadhabits.android.app.feature.PreviousMonthHabitEventCountFeature
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

fun createHabitEventTimeInputFeature(habitEventId: Int? = null) = HabitEventTimeInputFeature(
    coroutineScope = CoroutineScope(Dispatchers.Default),
    habitsRepository = get(),
    habitEventValidator = get(),
    habitEventId = habitEventId
)

fun createHabitEventCommentInputFeature(habitEventId: Int? = null) = HabitEventCommentInputFeature(
    coroutineScope = CoroutineScope(Dispatchers.Default),
    habitsRepository = get(),
    habitEventId = habitEventId
)

fun createHabitEventHabitIdFeature(habitEventId: Int) = HabitEventHabitIdFeature(
    coroutineScope = CoroutineScope(Dispatchers.Default),
    habitsRepository = get(),
    habitEventId = habitEventId
)

fun createHabitEventUpdatingFeature() = HabitEventUpdatingFeature(
    coroutineScope = CoroutineScope(Dispatchers.Default),
    habitsRepository = get()
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

fun createHabitAbstinenceTimesFeature(habitId: Int) = HabitAbstinenceTimesFeature(
    coroutineScope = CoroutineScope(Dispatchers.Default),
    habitsRepository = get(),
    habitId = habitId
)

fun createHabitAverageAbstinenceTimeFeature(habitId: Int) = HabitAverageAbstinenceTimeFeature(
    coroutineScope = CoroutineScope(Dispatchers.Default),
    habitAbstinenceTimesFeature = createHabitAbstinenceTimesFeature(habitId)
)

fun createHabitMaxAbstinenceTimeFeature(habitId: Int) = HabitMaxAbstinenceTimeFeature(
    coroutineScope = CoroutineScope(Dispatchers.Default),
    habitAbstinenceTimesFeature = createHabitAbstinenceTimesFeature(habitId)
)

fun createHabitMinAbstinenceTimeFeature(habitId: Int) = HabitMinAbstinenceTimeFeature(
    coroutineScope = CoroutineScope(Dispatchers.Default),
    habitAbstinenceTimesFeature = createHabitAbstinenceTimesFeature(habitId)
)

fun createHabitTimeSinceFirstEventFeature(habitId: Int) = HabitTimeSinceFirstEventFeature(
    coroutineScope = CoroutineScope(Dispatchers.Default),
    habitEventTimesFeature = createHabitEventTimesFeature(habitId)
)

fun createCurrentMonthHabitEventCountFeature(habitId: Int) = CurrentMonthHabitEventCountFeature(
    coroutineScope = CoroutineScope(Dispatchers.Default),
    habitsRepository = get(),
    habitId = habitId
)

fun createPreviousMonthHabitEventCountFeature(habitId: Int) = PreviousMonthHabitEventCountFeature(
    coroutineScope = CoroutineScope(Dispatchers.Default),
    habitsRepository = get(),
    habitId = habitId
)

fun createHabitEventCountFeature(habitId: Int) = HabitEventCountFeature(
    coroutineScope = CoroutineScope(Dispatchers.Default),
    habitsRepository = get(),
    habitId = habitId
)

fun createHabitEventCreationFeature() = HabitEventCreationFeature(
    coroutineScope = CoroutineScope(Dispatchers.Default),
    habitsRepository = get()
)

fun createHabitEventDeletionFeature() = HabitEventDeletionFeature(
    coroutineScope = CoroutineScope(Dispatchers.Default),
    habitsRepository = get()
)

fun createCurrentMonthHabitEventTimesFeature(habitId: Int) = CurrentMonthHabitEventTimesFeature(
    coroutineScope = CoroutineScope(Dispatchers.Default),
    habitEventTimesFeature = createHabitEventTimesFeature(habitId)
)

fun createCurrentMonthHabitEventIdsFeature(habitId: Int) = CurrentMonthHabitEventIdsFeature(
    coroutineScope = CoroutineScope(Dispatchers.Default),
    habitsRepository = get(),
    habitId = habitId
)

fun createHabitEventTimeFeature(habitEventId: Int) = HabitEventTimeFeature(
    coroutineScope = CoroutineScope(Dispatchers.Default),
    habitsRepository = get(),
    habitEventId = habitEventId
)

fun createHabitEventCommentFeature(habitEventId: Int) = HabitEventCommentFeature(
    coroutineScope = CoroutineScope(Dispatchers.Default),
    habitsRepository = get(),
    habitEventId = habitEventId
)

fun createHabitEventIdsFeature(habitId: Int) = HabitEventIdsFeature(
    coroutineScope = CoroutineScope(Dispatchers.Default),
    habitsRepository = get(),
    habitId = habitId
)

fun createHabitEventTimesFeature(habitId: Int) = HabitEventTimesFeature(
    coroutineScope = CoroutineScope(Dispatchers.Default),
    habitsRepository = get(),
    habitId = habitId
)



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