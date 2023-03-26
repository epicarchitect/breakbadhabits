package breakbadhabits.app.di

import android.content.Context
import breakbadhabits.app.database.AppDatabaseFactory
import breakbadhabits.app.database.IdGenerator
import breakbadhabits.app.logic.datetime.config.DateTimeConfigProvider
import breakbadhabits.app.logic.datetime.DateTimeProviderImpl
import breakbadhabits.app.logic.habits.config.HabitsConfigProvider
import breakbadhabits.app.logic.habits.HabitCreator
import breakbadhabits.app.logic.habits.tracks.HabitTrackCreator
import breakbadhabits.app.logic.habits.HabitDeleter
import breakbadhabits.app.logic.habits.tracks.HabitTrackDeleter
import breakbadhabits.app.logic.habits.HabitAbstinenceProvider
import breakbadhabits.app.logic.habits.HabitProvider
import breakbadhabits.app.logic.habits.HabitStatisticsProvider
import breakbadhabits.app.logic.habits.tracks.HabitTrackProvider
import breakbadhabits.app.logic.habits.tracks.HabitTrackUpdater
import breakbadhabits.app.logic.habits.HabitUpdater
import breakbadhabits.app.logic.habits.HabitNewNameValidator
import breakbadhabits.app.logic.habits.tracks.HabitTrackEventCountValidator
import breakbadhabits.app.logic.habits.tracks.HabitTrackTimeValidator
import breakbadhabits.app.logic.habits.appWidgetConfig.HabitAppWidgetConfigCreator
import breakbadhabits.app.logic.habits.appWidgetConfig.HabitAppWidgetConfigDeleter
import breakbadhabits.app.logic.habits.appWidgetConfig.HabitAppWidgetConfigProvider
import breakbadhabits.app.logic.habits.appWidgetConfig.HabitAppWidgetConfigUpdater

class LogicModule(
    private val context: Context,
    private val databaseName: String
) {
    private val appDatabase by lazy {
        AppDatabaseFactory.create(
            context = context,
            name = databaseName
        )
    }

    private val idGenerator by lazy {
        IdGenerator(context = context)
    }

    val dateTimeConfigProvider by lazy {
        DateTimeConfigProvider()
    }

    val dateTimeProvider by lazy {
        DateTimeProviderImpl(
            configProvider = dateTimeConfigProvider,
            coroutineDispatchers = DefaultCoroutineDispatchers
        )
    }

    val habitAbstinenceProvider by lazy {
        HabitAbstinenceProvider(
            habitTrackProvider = habitTrackProvider,
            dateTimeProvider = dateTimeProvider,
            coroutineDispatchers = DefaultCoroutineDispatchers
        )
    }

    val habitCreator by lazy {
        HabitCreator(
            appDatabase = appDatabase,
            idGenerator = idGenerator,
            coroutineDispatchers = DefaultCoroutineDispatchers
        )
    }

    private val habitsConfigProvider by lazy {
        HabitsConfigProvider()
    }

    val habitNewNameValidator by lazy {
        HabitNewNameValidator(
            appDatabase = appDatabase,
            configProvider = habitsConfigProvider,
            coroutineDispatchers = DefaultCoroutineDispatchers
        )
    }

    val habitTrackTimeValidator by lazy {
        HabitTrackTimeValidator(dateTimeProvider = dateTimeProvider)
    }

    val habitTrackEventCountValidator by lazy {
        HabitTrackEventCountValidator()
    }

    val habitDeleter by lazy {
        HabitDeleter(
            appDatabase = appDatabase,
            coroutineDispatchers = DefaultCoroutineDispatchers
        )
    }

    val habitIconProvider by lazy {
        HabitIconProvider()
    }

    val habitProvider by lazy {
        HabitProvider(
            appDatabase = appDatabase,
            coroutineDispatchers = DefaultCoroutineDispatchers
        )
    }

    val habitUpdater by lazy {
        HabitUpdater(
            appDatabase = appDatabase,
            coroutineDispatchers = DefaultCoroutineDispatchers
        )
    }

    val habitTrackUpdater by lazy {
        HabitTrackUpdater(
            appDatabase = appDatabase,
            coroutineDispatchers = DefaultCoroutineDispatchers
        )
    }

    val habitTrackDeleter by lazy {
        HabitTrackDeleter(
            appDatabase = appDatabase,
            coroutineDispatchers = DefaultCoroutineDispatchers
        )
    }

    val habitStatisticsProvider by lazy {
        HabitStatisticsProvider(
            habitTrackProvider = habitTrackProvider,
            habitAbstinenceProvider = habitAbstinenceProvider,
            dateTimeProvider = dateTimeProvider,
            dateTimeConfigProvider = dateTimeConfigProvider,
            coroutineDispatchers = DefaultCoroutineDispatchers
        )
    }
    val habitTrackCreator by lazy {
        HabitTrackCreator(
            appDatabase = appDatabase,
            idGenerator = idGenerator,
            coroutineDispatchers = DefaultCoroutineDispatchers
        )
    }

    val habitTrackProvider by lazy {
        HabitTrackProvider(
            appDatabase = appDatabase,
            dateTimeConfigProvider = dateTimeConfigProvider,
            coroutineDispatchers = DefaultCoroutineDispatchers
        )
    }

    val habitAppWidgetConfigProvider by lazy {
        HabitAppWidgetConfigProvider(
            coroutineDispatchers = DefaultCoroutineDispatchers,
            appDatabase = appDatabase
        )
    }

    val habitAppWidgetConfigCreator by lazy {
        HabitAppWidgetConfigCreator(
            appDatabase = appDatabase,
            idGenerator = idGenerator,
            coroutineDispatchers = DefaultCoroutineDispatchers
        )
    }

    val habitAppWidgetConfigUpdater by lazy {
        HabitAppWidgetConfigUpdater(
            appDatabase = appDatabase,
            coroutineDispatchers = DefaultCoroutineDispatchers
        )
    }

    val habitAppWidgetConfigDeleter by lazy {
        HabitAppWidgetConfigDeleter(
            appDatabase = appDatabase,
            coroutineDispatchers = DefaultCoroutineDispatchers
        )
    }
}