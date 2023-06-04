package epicarchitect.breakbadhabits.android.app.di

import android.content.Context
import epicarchitect.breakbadhabits.android.app.coroutines.DefaultCoroutineDispatchers
import epicarchitect.breakbadhabits.android.app.icons.HabitIconProvider
import epicarchitect.breakbadhabits.database.IdGenerator
import epicarchitect.breakbadhabits.database.main.MainDatabaseFactory
import epicarchitect.breakbadhabits.logic.datetime.provider.DateTimeProvider
import epicarchitect.breakbadhabits.logic.habits.creator.HabitCreator
import epicarchitect.breakbadhabits.logic.habits.creator.HabitTrackCreator
import epicarchitect.breakbadhabits.logic.habits.creator.HabitWidgetCreator
import epicarchitect.breakbadhabits.logic.habits.deleter.HabitDeleter
import epicarchitect.breakbadhabits.logic.habits.deleter.HabitTrackDeleter
import epicarchitect.breakbadhabits.logic.habits.deleter.HabitWidgetDeleter
import epicarchitect.breakbadhabits.logic.habits.provider.HabitAbstinenceProvider
import epicarchitect.breakbadhabits.logic.habits.provider.HabitProvider
import epicarchitect.breakbadhabits.logic.habits.provider.HabitStatisticsProvider
import epicarchitect.breakbadhabits.logic.habits.provider.HabitTrackProvider
import epicarchitect.breakbadhabits.logic.habits.provider.HabitWidgetProvider
import epicarchitect.breakbadhabits.logic.habits.provider.HabitsConfigProvider
import epicarchitect.breakbadhabits.logic.habits.updater.HabitTrackUpdater
import epicarchitect.breakbadhabits.logic.habits.updater.HabitUpdater
import epicarchitect.breakbadhabits.logic.habits.updater.HabitWidgetUpdater
import epicarchitect.breakbadhabits.logic.habits.validator.HabitNewNameValidator
import epicarchitect.breakbadhabits.logic.habits.validator.HabitTrackEventCountValidator
import epicarchitect.breakbadhabits.logic.habits.validator.HabitTrackTimeValidator

class LogicModule(
    private val context: Context,
    private val databaseName: String
) {
    private val appDatabase by lazy {
        MainDatabaseFactory.create(
            context = context,
            name = databaseName
        )
    }

    private val idGenerator by lazy {
        IdGenerator(context = context)
    }

    val coroutineDispatchers by lazy {
        DefaultCoroutineDispatchers()
    }

    val localIconProvider by lazy {
        HabitIconProvider()
    }

    val dateTimeProvider: DateTimeProvider by lazy {
        DateTimeProvider(
            coroutineDispatchers = coroutineDispatchers
        )
    }

    val habitAbstinenceProvider by lazy {
        HabitAbstinenceProvider(
            habitTrackProvider = habitTrackProvider,
            dateTimeProvider = dateTimeProvider,
            coroutineDispatchers = coroutineDispatchers
        )
    }

    val habitCreator by lazy {
        HabitCreator(
            mainDatabase = appDatabase,
            idGenerator = idGenerator,
            coroutineDispatchers = coroutineDispatchers
        )
    }

    private val habitsConfigProvider by lazy {
        HabitsConfigProvider()
    }

    val habitNewNameValidator by lazy {
        HabitNewNameValidator(
            mainDatabase = appDatabase,
            configProvider = habitsConfigProvider,
            coroutineDispatchers = coroutineDispatchers
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
            mainDatabase = appDatabase,
            coroutineDispatchers = coroutineDispatchers
        )
    }

    val habitProvider by lazy {
        HabitProvider(
            mainDatabase = appDatabase,
            coroutineDispatchers = coroutineDispatchers,
            iconProvider = localIconProvider
        )
    }

    val habitUpdater by lazy {
        HabitUpdater(
            mainDatabase = appDatabase,
            coroutineDispatchers = coroutineDispatchers
        )
    }

    val habitTrackUpdater by lazy {
        HabitTrackUpdater(
            mainDatabase = appDatabase,
            coroutineDispatchers = coroutineDispatchers
        )
    }

    val habitTrackDeleter by lazy {
        HabitTrackDeleter(
            mainDatabase = appDatabase,
            coroutineDispatchers = coroutineDispatchers
        )
    }

    val habitStatisticsProvider by lazy {
        HabitStatisticsProvider(
            habitTrackProvider = habitTrackProvider,
            habitAbstinenceProvider = habitAbstinenceProvider,
            dateTimeProvider = dateTimeProvider,
            coroutineDispatchers = coroutineDispatchers
        )
    }
    val habitTrackCreator by lazy {
        HabitTrackCreator(
            mainDatabase = appDatabase,
            idGenerator = idGenerator,
            coroutineDispatchers = coroutineDispatchers
        )
    }

    val habitTrackProvider by lazy {
        HabitTrackProvider(
            mainDatabase = appDatabase,
            dateTimeProvider = dateTimeProvider,
            coroutineDispatchers = coroutineDispatchers
        )
    }

    val habitWidgetProvider by lazy {
        HabitWidgetProvider(
            coroutineDispatchers = coroutineDispatchers,
            appDatabase = appDatabase
        )
    }

    val habitWidgetCreator by lazy {
        HabitWidgetCreator(
            mainDatabase = appDatabase,
            idGenerator = idGenerator,
            coroutineDispatchers = coroutineDispatchers
        )
    }

    val habitWidgetUpdater by lazy {
        HabitWidgetUpdater(
            mainDatabase = appDatabase,
            coroutineDispatchers = coroutineDispatchers
        )
    }

    val habitWidgetDeleter by lazy {
        HabitWidgetDeleter(
            mainDatabase = appDatabase,
            coroutineDispatchers = coroutineDispatchers
        )
    }
}