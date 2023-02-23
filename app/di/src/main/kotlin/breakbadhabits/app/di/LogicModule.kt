package breakbadhabits.app.di

import android.content.Context
import breakbadhabits.app.database.AppDatabaseFactory
import breakbadhabits.app.database.IdGenerator
import breakbadhabits.app.logic.datetime.formatter.DateTimeFormatter
import breakbadhabits.app.logic.datetime.provider.DateTimeProvider
import breakbadhabits.app.logic.habits.creator.HabitCreator
import breakbadhabits.app.logic.habits.creator.HabitTrackCreator
import breakbadhabits.app.logic.habits.deleter.HabitDeleter
import breakbadhabits.app.logic.habits.provider.HabitIconProvider
import breakbadhabits.app.logic.habits.provider.HabitProvider
import breakbadhabits.app.logic.habits.provider.HabitTrackProvider
import breakbadhabits.app.logic.habits.validator.HabitNewNameValidator
import breakbadhabits.app.logic.habits.validator.HabitTrackIntervalValidator

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

    val dateTimeProvider by lazy {
        DateTimeProvider(updatePeriodMillis = 1000L)
    }

    val dateTimeFormatter by lazy {
        DateTimeFormatter(
            secondText = context.getString(R.string.s),
            minuteText = context.getString(R.string.m),
            hourText = context.getString(R.string.h),
            dayText = context.getString(R.string.d),
        )
    }

    val habitCreator by lazy {
        HabitCreator(appDatabase, idGenerator)
    }

    val habitNewNameValidator by lazy {
        HabitNewNameValidator(appDatabase, maxNameLength = 30)
    }

    val habitTrackIntervalValidator by lazy {
        HabitTrackIntervalValidator(getCurrentTime = dateTimeProvider::getCurrentTime)
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

    val habitTrackCreator by lazy {
        HabitTrackCreator(appDatabase, idGenerator)
    }

    val habitTrackProvider by lazy {
        HabitTrackProvider(appDatabase)
    }
}