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
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module
import epicarchitect.architecture.FlowDrivenArchitecture
import org.koin.dsl.module

class BreakBadHabitsApp : Application() {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val habitsRepository: HabitsRepository by inject()
    private val habitAppWidgetsRepository: AppWidgetsRepository by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@BreakBadHabitsApp)
            modules(
                module {
                    app()
                    habits()
                    appWidgets()
                }
            )
        }

        combine(
            habitsRepository.habitListFlow(),
            habitsRepository.habitEventListFlow(),
            habitAppWidgetsRepository.habitsAppWidgetConfigListFlow()
        ) { _, _, _ ->
            HabitsAppWidgetProvider.sendUpdateIntent(this@BreakBadHabitsApp)
        }.launchIn(coroutineScope)
    }

    companion object {
        val architecture by lazy {
            FlowDrivenArchitecture {

            }
        }
    }
}

private fun Module.app() {
    single { AbstinenceTimeFormatter(androidApplication()) }
    single { DateTimeFormatter(androidApplication()) }
    single { AlertDialogManager() }
    single {
        BreakBadHabitsAppConfig(
            maxHabitNameLength = 30
        )
    }
    single {
        Room.databaseBuilder(androidApplication(), MainDatabase::class.java, "main.db").build()
    }
    single { IdGenerator(androidApplication()) }
    single(createdAtStart = true) { NightModeManager(androidApplication()) }
}

private fun Module.appWidgets() {
    single { AppWidgetsRepository(get(), get()) }
    factory { (appWidgetId: Int) -> HabitsAppWidgetConfigCreationViewModel(get(), get(), appWidgetId) }
    factory { (configId: Int) -> HabitsAppWidgetConfigEditingViewModel(get(), get(), configId) }
    factory { WidgetsViewModel(get(), get()) }
}

private fun Module.habits() {
    single { HabitIconResources(androidApplication()) }
    single { HabitsRepository(get(), get()) }
    factory { HabitValidator(get<HabitsRepository>()::habitNameExists, get<BreakBadHabitsAppConfig>().maxHabitNameLength) }
    factory { HabitEventValidator { System.currentTimeMillis() } }
    factory { HabitCreationViewModel(get(), get(), get()) }
    factory { HabitsViewModel(get()) }
    factory { (habitId: Int) -> HabitViewModel(get(), habitId) }
    factory { (habitId: Int) -> HabitAnalyzeViewModel(get(), habitId) }
    factory { (habitId: Int) -> HabitEventCreationViewModel(get(), get(), habitId) }
    factory { (habitId: Int) -> HabitEventsViewModel(get(), habitId) }
    factory { (habitId: Int) -> HabitEditingViewModel(get(), get(), habitId) }
    factory { (habitId: Int) -> HabitDeletionViewModel(get(), habitId) }
    factory { (habitEventId: Int) -> HabitEventEditingViewModel(get(), get(), habitEventId) }
}