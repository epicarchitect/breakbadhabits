package breakbadhabits.app.di

import android.content.Context
import breakbadhabits.app.database.AppDatabaseFactory
import breakbadhabits.app.database.IdGenerator
import breakbadhabits.app.logic.datetime.config.DateTimeConfigProvider
import breakbadhabits.app.logic.datetime.provider.DateTimeProvider
import breakbadhabits.app.logic.habits.config.HabitsConfigProvider
import breakbadhabits.app.logic.habits.creator.HabitCreator
import breakbadhabits.app.logic.habits.creator.HabitTrackCreator
import breakbadhabits.app.logic.habits.deleter.HabitDeleter
import breakbadhabits.app.logic.habits.deleter.HabitTrackDeleter
import breakbadhabits.app.logic.habits.provider.HabitAbstinenceProvider
import breakbadhabits.app.logic.habits.provider.HabitIconProvider
import breakbadhabits.app.logic.habits.provider.HabitProvider
import breakbadhabits.app.logic.habits.provider.HabitStatisticsProvider
import breakbadhabits.app.logic.habits.provider.HabitTrackProvider
import breakbadhabits.app.logic.habits.updater.HabitTrackUpdater
import breakbadhabits.app.logic.habits.updater.HabitUpdater
import breakbadhabits.app.logic.habits.validator.HabitNewNameValidator
import breakbadhabits.app.logic.habits.validator.HabitTrackEventCountValidator
import breakbadhabits.app.logic.habits.validator.HabitTrackTimeValidator

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

    private val dateTimeProvider by lazy {
        DateTimeProvider(configProvider = dateTimeConfigProvider)
    }

    val habitAbstinenceProvider by lazy {
        HabitAbstinenceProvider(
            habitTrackProvider = habitTrackProvider,
            dateTimeProvider = dateTimeProvider
        )
    }

    val habitCreator by lazy {
        HabitCreator(
            appDatabase = appDatabase,
            idGenerator = idGenerator,
            dateTimeConfigProvider = dateTimeConfigProvider,
            dateTimeProvider = dateTimeProvider
        )
    }

    private val habitsConfigProvider by lazy {
        HabitsConfigProvider()
    }

    val habitNewNameValidator by lazy {
        HabitNewNameValidator(
            appDatabase = appDatabase,
            configProvider = habitsConfigProvider
        )
    }

    val habitTrackTimeValidator by lazy {
        HabitTrackTimeValidator(dateTimeProvider = dateTimeProvider)
    }

    val habitTrackEventCountValidator by lazy {
        HabitTrackEventCountValidator()
    }

    val habitDeleter by lazy {
        HabitDeleter(appDatabase = appDatabase)
    }

    val habitIconProvider by lazy {
        HabitIconProvider()
    }

    val habitProvider by lazy {
        HabitProvider(appDatabase = appDatabase)
    }

    val habitUpdater by lazy {
        HabitUpdater(appDatabase = appDatabase)
    }

    val habitTrackUpdater by lazy {
        HabitTrackUpdater(
            appDatabase = appDatabase,
            dateTimeConfigProvider = dateTimeConfigProvider
        )
    }

    val habitTrackDeleter by lazy {
        HabitTrackDeleter(appDatabase = appDatabase)
    }

    val habitStatisticsProvider by lazy {
        HabitStatisticsProvider(
            habitTrackProvider = habitTrackProvider,
            habitAbstinenceProvider = habitAbstinenceProvider,
            dateTimeProvider = dateTimeProvider,
            dateTimeConfigProvider = dateTimeConfigProvider
        )
    }
    val habitTrackCreator by lazy {
        HabitTrackCreator(
            appDatabase = appDatabase,
            idGenerator = idGenerator,
            dateTimeConfigProvider = dateTimeConfigProvider,
            dateTimeProvider = dateTimeProvider
        )
    }

    val habitTrackProvider by lazy {
        HabitTrackProvider(
            appDatabase = appDatabase,
            dateTimeConfigProvider = dateTimeConfigProvider
        )
    }
}