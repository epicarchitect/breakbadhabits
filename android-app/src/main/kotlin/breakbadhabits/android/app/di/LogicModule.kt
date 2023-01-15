package breakbadhabits.android.app.di

import android.content.Context
import breakbadhabits.database.AppDatabaseFactory
import breakbadhabits.database.IdGenerator
import breakbadhabits.logic.HabitAbstinenceProvider
import breakbadhabits.logic.HabitCreator
import breakbadhabits.logic.HabitDeleter
import breakbadhabits.logic.HabitIconsProvider
import breakbadhabits.logic.HabitNewNameValidator
import breakbadhabits.logic.HabitProvider
import breakbadhabits.logic.HabitTrackCreator
import breakbadhabits.logic.HabitTrackIntervalValidator
import breakbadhabits.logic.HabitTrackProvider
import breakbadhabits.logic.TimeProvider

class LogicModule(private val context: Context) {

    private val appDatabase by lazy {
        AppDatabaseFactory().create(context)
    }

    private val idGenerator by lazy {
        IdGenerator(context)
    }

    val timeProvider by lazy {
        TimeProvider(updatePeriodMillis = 1000L)
    }

    val habitAbstinenceProvider by lazy {
        HabitAbstinenceProvider(appDatabase, timeProvider)
    }

    val habitCreator by lazy {
        HabitCreator(appDatabase, idGenerator)
    }

    val habitNewNameValidator by lazy {
        HabitNewNameValidator(appDatabase, maxNameLength = 30)
    }

    val habitTrackIntervalValidator by lazy {
        HabitTrackIntervalValidator(getCurrentTime = timeProvider::getCurrentTime)
    }

    val habitDeleter by lazy {
        HabitDeleter(appDatabase)
    }

    val habitIconsProvider by lazy {
        HabitIconsProvider()
    }

    val habitProvider by lazy {
        HabitProvider(appDatabase)
    }

    val habitTrackCreator by lazy {
        HabitTrackCreator(appDatabase, idGenerator)
    }

    val habitTrackProvider by lazy {
        HabitTrackProvider(appDatabase)
    }
}