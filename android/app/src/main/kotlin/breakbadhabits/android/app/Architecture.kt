package breakbadhabits.android.app

import android.app.Application
import androidx.room.Room
import breakbadhabits.android.app.appwidget.HabitsAppWidgetProvider
import breakbadhabits.android.app.config.BreakBadHabitsAppConfig
import breakbadhabits.android.app.database.MainDatabase
import breakbadhabits.android.app.formatter.AbstinenceTimeFormatter
import breakbadhabits.android.app.formatter.DateTimeFormatter
import breakbadhabits.android.app.repository.AppWidgetsRepository
import breakbadhabits.android.app.repository.HabitsRepository
import breakbadhabits.android.app.repository.IdGenerator
import breakbadhabits.android.app.resources.HabitIconResources
import breakbadhabits.android.app.utils.AlertDialogManager
import breakbadhabits.android.app.utils.NightModeManager
import breakbadhabits.android.app.validator.HabitEventValidator
import breakbadhabits.android.app.validator.HabitValidator
import breakbadhabits.android.app.viewmodel.HabitAnalyzeViewModel
import breakbadhabits.android.app.viewmodel.HabitCreationViewModel
import breakbadhabits.android.app.viewmodel.HabitDeletionViewModel
import breakbadhabits.android.app.viewmodel.HabitEditingViewModel
import breakbadhabits.android.app.viewmodel.HabitEventCreationViewModel
import breakbadhabits.android.app.viewmodel.HabitEventEditingViewModel
import breakbadhabits.android.app.viewmodel.HabitEventsViewModel
import breakbadhabits.android.app.viewmodel.HabitViewModel
import breakbadhabits.android.app.viewmodel.HabitsAppWidgetConfigCreationViewModel
import breakbadhabits.android.app.viewmodel.HabitsAppWidgetConfigEditingViewModel
import breakbadhabits.android.app.viewmodel.HabitsViewModel
import breakbadhabits.android.app.viewmodel.WidgetsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn

class Architecture(application: Application) {

    private val breakBadHabitsConfig = BreakBadHabitsAppConfig(maxHabitNameLength = 30)
    private val mainDatabase = Room.databaseBuilder(application, MainDatabase::class.java, "main.db").build()
    private val idGenerator = IdGenerator(application)
    val abstinenceTimeFormatter = AbstinenceTimeFormatter(application)
    val dateTimeFormatter = DateTimeFormatter(application)
    val alertDialogManager = AlertDialogManager()
    val habitIconResources = HabitIconResources(application)
    val nightModeManager = NightModeManager(application)
    val appWidgetsRepository = AppWidgetsRepository(idGenerator, mainDatabase)
    val habitsRepository = HabitsRepository(idGenerator, mainDatabase)

    init {
        combine(
            habitsRepository.habitListFlow(),
            habitsRepository.habitEventListFlow(),
            appWidgetsRepository.habitsAppWidgetConfigListFlow()
        ) { _, _, _ ->
            HabitsAppWidgetProvider.sendUpdateIntent(application)
        }.launchIn(CoroutineScope(Dispatchers.Default))
    }

    fun createHabitsAppWidgetConfigCreationViewModel(appWidgetId: Int) = HabitsAppWidgetConfigCreationViewModel(
        habitsRepository,
        appWidgetsRepository,
        appWidgetId
    )

    fun createHabitsAppWidgetConfigEditingViewModel(configId: Int) = HabitsAppWidgetConfigEditingViewModel(
        habitsRepository,
        appWidgetsRepository,
        configId
    )

    fun createWidgetsViewModel() = WidgetsViewModel(
        habitsRepository,
        appWidgetsRepository
    )

    fun createHabitValidator() = HabitValidator(
        habitsRepository::habitNameExists,
        breakBadHabitsConfig.maxHabitNameLength
    )

    fun createHabitEventValidator() = HabitEventValidator { System.currentTimeMillis() }

    fun createHabitCreationViewModel() = HabitCreationViewModel(
        habitsRepository,
        createHabitValidator(),
        createHabitEventValidator()
    )

    fun createHabitsViewModel() = HabitsViewModel(habitsRepository)

    fun createHabitViewModel(habitId: Int) = HabitViewModel(
        habitsRepository,
        habitId
    )

    fun createHabitAnalyzeViewModel(habitId: Int) = HabitAnalyzeViewModel(
        habitsRepository,
        habitId
    )

    fun createHabitEventCreationViewModel(habitId: Int) = HabitEventCreationViewModel(
        habitsRepository,
        createHabitEventValidator(),
        habitId
    )

    fun createHabitEventsViewModel(habitId: Int) = HabitEventsViewModel(
        habitsRepository,
        habitId
    )

    fun createHabitEditingViewModel(habitId: Int) = HabitEditingViewModel(
        habitsRepository,
        createHabitValidator(),
        habitId
    )

    fun createHabitDeletionViewModel(habitId: Int) = HabitDeletionViewModel(
        habitsRepository,
        habitId
    )

    fun createHabitEventEditingViewModel(habitEventId: Int) = HabitEventEditingViewModel(
        habitsRepository,
        createHabitEventValidator(),
        habitEventId
    )
}