package breakbadhabits.android.app.di

import android.content.Context
import androidx.room.Room
import breakbadhabits.android.app.database.MainDatabase
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
import breakbadhabits.android.app.feature.HabitEventTimeInputFeature
import breakbadhabits.android.app.feature.HabitEventTimesFeature
import breakbadhabits.android.app.feature.HabitEventUpdatingFeature
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
import breakbadhabits.android.app.feature.HabitsAppWidgetCreationFeature
import breakbadhabits.android.app.feature.HabitsAppWidgetDeletionFeature
import breakbadhabits.android.app.feature.HabitsAppWidgetHabitIdsFeature
import breakbadhabits.android.app.feature.HabitsAppWidgetHabitIdsSelectionFeature
import breakbadhabits.android.app.feature.HabitsAppWidgetIdFeature
import breakbadhabits.android.app.feature.HabitsAppWidgetTitleFeature
import breakbadhabits.android.app.feature.HabitsAppWidgetTitleInputFeature
import breakbadhabits.android.app.feature.HabitsAppWidgetUpdatingFeature
import breakbadhabits.android.app.feature.PreviousMonthHabitEventCountFeature
import breakbadhabits.android.app.formatter.AbstinenceTimeFormatter
import breakbadhabits.android.app.formatter.DateTimeFormatter
import breakbadhabits.android.app.repository.AppWidgetsRepository
import breakbadhabits.android.app.repository.HabitsRepository
import breakbadhabits.android.app.repository.IdGenerator
import breakbadhabits.android.app.resources.HabitIconResources
import breakbadhabits.android.app.utils.AlertDialogManager
import breakbadhabits.android.app.validator.HabitEventValidator
import breakbadhabits.android.app.validator.HabitValidator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class AppDependencies(context: Context) {

    val abstinenceTimeFormatter = AbstinenceTimeFormatter(context)
    val dateTimeFormatter = DateTimeFormatter(context)
    val alertDialogManager = AlertDialogManager()
    private val idGenerator = IdGenerator(context)
    val habitIconResources = HabitIconResources(context)
    private val mainDatabase = Room.databaseBuilder(
        context,
        MainDatabase::class.java,
        "main.db"
    ).build()

    val appWidgetsRepository = AppWidgetsRepository(
        idGenerator,
        mainDatabase
    )

    val habitsRepository = HabitsRepository(
        idGenerator,
        mainDatabase
    )

    val habitValidator = HabitValidator(
        habitsRepository::habitNameExists,
        30
    )

    val habitEventValidator = HabitEventValidator(
        getCurrentTimeInMillis = { System.currentTimeMillis() }
    )

    fun createHabitIdsFeature() = HabitIdsFeature(
        coroutineScope = CoroutineScope(Dispatchers.Default),
        habitsRepository = habitsRepository
    )

    fun createHabitIconIdFeature(habitId: Int) = HabitIconIdFeature(
        coroutineScope = CoroutineScope(Dispatchers.Default),
        habitsRepository = habitsRepository,
        habitId = habitId
    )

    fun createHabitAbstinenceTimeFeature(habitId: Int) = HabitAbstinenceTimeFeature(
        coroutineScope = CoroutineScope(Dispatchers.Default),
        habitsRepository = habitsRepository,
        habitId = habitId
    )

    fun createHabitNameFeature(habitId: Int) = HabitNameFeature(
        coroutineScope = CoroutineScope(Dispatchers.Default),
        habitsRepository = habitsRepository,
        habitId = habitId
    )

    fun createHabitNameInputFeature(habitId: Int? = null) = HabitNameInputFeature(
        coroutineScope = CoroutineScope(Dispatchers.Default),
        habitsRepository = habitsRepository,
        habitValidator = habitValidator,
        habitId = habitId
    )

    fun createHabitEventTimeInputFeature(habitEventId: Int? = null) = HabitEventTimeInputFeature(
        coroutineScope = CoroutineScope(Dispatchers.Default),
        habitsRepository = habitsRepository,
        habitEventValidator = habitEventValidator,
        habitEventId = habitEventId
    )

    fun createHabitEventCommentInputFeature(habitEventId: Int? = null) =
        HabitEventCommentInputFeature(
            coroutineScope = CoroutineScope(Dispatchers.Default),
            habitsRepository = habitsRepository,
            habitEventId = habitEventId
        )

    fun createHabitEventHabitIdFeature(habitEventId: Int) = HabitEventHabitIdFeature(
        coroutineScope = CoroutineScope(Dispatchers.Default),
        habitsRepository = habitsRepository,
        habitEventId = habitEventId
    )

    fun createHabitEventUpdatingFeature() = HabitEventUpdatingFeature(
        coroutineScope = CoroutineScope(Dispatchers.Default),
        habitsRepository = habitsRepository
    )

    fun createHabitIconSelectionFeature(habitId: Int? = null) = HabitIconSelectionFeature(
        coroutineScope = CoroutineScope(Dispatchers.Default),
        habitsRepository = habitsRepository,
        habitId = habitId
    )

    fun createHabitCreationFeature() = HabitCreationFeature(
        coroutineScope = CoroutineScope(Dispatchers.Default),
        habitsRepository = habitsRepository
    )

    fun createHabitDeletionFeature(habitId: Int) = HabitDeletionFeature(
        coroutineScope = CoroutineScope(Dispatchers.Default),
        habitsRepository = habitsRepository,
        habitId = habitId
    )

    fun createHabitUpdatingFeature(habitId: Int) = HabitUpdatingFeature(
        coroutineScope = CoroutineScope(Dispatchers.Default),
        habitsRepository = habitsRepository,
        habitId = habitId
    )

    fun createHabitAbstinenceTimesFeature(habitId: Int) = HabitAbstinenceTimesFeature(
        coroutineScope = CoroutineScope(Dispatchers.Default),
        habitsRepository = habitsRepository,
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
        habitsRepository = habitsRepository,
        habitId = habitId
    )

    fun createPreviousMonthHabitEventCountFeature(habitId: Int) =
        PreviousMonthHabitEventCountFeature(
            coroutineScope = CoroutineScope(Dispatchers.Default),
            habitsRepository = habitsRepository,
            habitId = habitId
        )

    fun createHabitEventCountFeature(habitId: Int) = HabitEventCountFeature(
        coroutineScope = CoroutineScope(Dispatchers.Default),
        habitsRepository = habitsRepository,
        habitId = habitId
    )

    fun createHabitEventCreationFeature() = HabitEventCreationFeature(
        coroutineScope = CoroutineScope(Dispatchers.Default),
        habitsRepository = habitsRepository
    )

    fun createHabitEventDeletionFeature() = HabitEventDeletionFeature(
        coroutineScope = CoroutineScope(Dispatchers.Default),
        habitsRepository = habitsRepository
    )

    fun createCurrentMonthHabitEventTimesFeature(habitId: Int) = CurrentMonthHabitEventTimesFeature(
        coroutineScope = CoroutineScope(Dispatchers.Default),
        habitEventTimesFeature = createHabitEventTimesFeature(habitId)
    )

    fun createCurrentMonthHabitEventIdsFeature(habitId: Int) = CurrentMonthHabitEventIdsFeature(
        coroutineScope = CoroutineScope(Dispatchers.Default),
        habitsRepository = habitsRepository,
        habitId = habitId
    )

    fun createHabitEventTimeFeature(habitEventId: Int) = HabitEventTimeFeature(
        coroutineScope = CoroutineScope(Dispatchers.Default),
        habitsRepository = habitsRepository,
        habitEventId = habitEventId
    )

    fun createHabitEventCommentFeature(habitEventId: Int) = HabitEventCommentFeature(
        coroutineScope = CoroutineScope(Dispatchers.Default),
        habitsRepository = habitsRepository,
        habitEventId = habitEventId
    )

    fun createHabitEventIdsFeature(habitId: Int) = HabitEventIdsFeature(
        coroutineScope = CoroutineScope(Dispatchers.Default),
        habitsRepository = habitsRepository,
        habitId = habitId
    )

    fun createHabitEventTimesFeature(habitId: Int) = HabitEventTimesFeature(
        coroutineScope = CoroutineScope(Dispatchers.Default),
        habitsRepository = habitsRepository,
        habitId = habitId
    )

    fun createHabitsAppWidgetConfigIdsFeature() = HabitsAppWidgetConfigIdsFeature(
        coroutineScope = CoroutineScope(Dispatchers.Default),
        appWidgetsRepository = appWidgetsRepository
    )

    fun createHabitsAppWidgetTitleFeature(configId: Int) = HabitsAppWidgetTitleFeature(
        coroutineScope = CoroutineScope(Dispatchers.Default),
        appWidgetsRepository = appWidgetsRepository,
        configId = configId
    )

    fun createHabitsAppWidgetHabitIdsFeature(configId: Int) = HabitsAppWidgetHabitIdsFeature(
        coroutineScope = CoroutineScope(Dispatchers.Default),
        appWidgetsRepository = appWidgetsRepository,
        configId = configId
    )

    fun createHabitsAppWidgetHabitIdsSelectionFeature(configId: Int? = null) =
        HabitsAppWidgetHabitIdsSelectionFeature(
            coroutineScope = CoroutineScope(Dispatchers.Default),
            habitsRepository = habitsRepository,
            appWidgetsRepository = appWidgetsRepository,
            configId = configId
        )

    fun createHabitsAppWidgetTitleInputFeature(configId: Int? = null) =
        HabitsAppWidgetTitleInputFeature(
            coroutineScope = CoroutineScope(Dispatchers.Default),
            appWidgetsRepository = appWidgetsRepository,
            configId = configId
        )

    fun createHabitsAppWidgetCreationFeature() = HabitsAppWidgetCreationFeature(
        coroutineScope = CoroutineScope(Dispatchers.Default),
        appWidgetsRepository = appWidgetsRepository
    )

    fun createHabitsAppWidgetUpdatingFeature() = HabitsAppWidgetUpdatingFeature(
        coroutineScope = CoroutineScope(Dispatchers.Default),
        appWidgetsRepository = appWidgetsRepository
    )

    fun createHabitsAppWidgetDeletionFeature() = HabitsAppWidgetDeletionFeature(
        coroutineScope = CoroutineScope(Dispatchers.Default),
        appWidgetsRepository = appWidgetsRepository
    )

    fun createHabitsAppWidgetIdFeature(configId: Int) = HabitsAppWidgetIdFeature(
        coroutineScope = CoroutineScope(Dispatchers.Default),
        appWidgetsRepository = appWidgetsRepository,
        configId = configId
    )
}