package breakbadhabits.app.di

import android.content.Context
import breakbadhabits.app.database.AppDatabaseFactory
import breakbadhabits.app.database.IdGenerator
import breakbadhabits.app.logic.datetime.provider.DateTimeProvider
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
import breakbadhabits.app.logic.habits.validator.HabitTrackRangeValidator

class LogicModule(private val context: Context) {

    private val appDatabase by lazy {
        AppDatabaseFactory.create(
            context,
            name = "breakbadhabits-v4.db"
        )
    }

    private val idGenerator by lazy {
        IdGenerator(context)
    }

    private val dateTimeProvider by lazy {
        DateTimeProvider(updatePeriodMillis = { 1000L })
    }

    val habitAbstinenceProvider by lazy {
        HabitAbstinenceProvider(
            habitTrackProvider,
            dateTimeProvider
        )
    }

    val habitCreator by lazy {
        HabitCreator(appDatabase, idGenerator)
    }

    val habitNewNameValidator by lazy {
        HabitNewNameValidator(appDatabase, maxNameLength = 30)
    }

    val habitTrackRangeValidator by lazy {
        HabitTrackRangeValidator(getCurrentTime = dateTimeProvider::getCurrentTime)
    }

    val habitTrackEventCountValidator by lazy {
        HabitTrackEventCountValidator()
    }

    val habitDeleter by lazy {
        HabitDeleter(appDatabase)
    }

    val habitIconProvider by lazy {
        HabitIconProvider()
    }

    val habitProvider by lazy {
        HabitProvider(appDatabase)
    }

    val habitUpdater by lazy {
        HabitUpdater(appDatabase)
    }

    val habitTrackUpdater by lazy {
        HabitTrackUpdater(appDatabase)
    }

    val habitTrackDeleter by lazy {
        HabitTrackDeleter(appDatabase)
    }

    val habitStatisticsProvider by lazy {
        HabitStatisticsProvider(habitTrackProvider, habitAbstinenceProvider, dateTimeProvider)
    }

    val habitTrackCreator by lazy {
        HabitTrackCreator(appDatabase, idGenerator)
    }

    val habitTrackProvider by lazy {
        HabitTrackProvider(appDatabase)
    }
}