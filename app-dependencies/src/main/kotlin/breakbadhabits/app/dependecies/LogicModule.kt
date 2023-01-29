package breakbadhabits.app.dependecies

import android.content.Context
import breakbadhabits.database.AppDatabaseFactory
import breakbadhabits.database.IdGenerator
import breakbadhabits.logic.DateTimeProvider
import breakbadhabits.logic.DateTimeRangeFormatter
import breakbadhabits.logic.HabitCreator
import breakbadhabits.logic.HabitDeleter
import breakbadhabits.logic.HabitIconsProvider
import breakbadhabits.logic.HabitNewNameValidator
import breakbadhabits.logic.HabitProvider
import breakbadhabits.logic.HabitTrackCreator
import breakbadhabits.logic.HabitTrackIntervalValidator
import breakbadhabits.logic.HabitTrackProvider

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
        DateTimeProvider(updatePeriodMillis = 5000L)
    }

    val dateTimeRangeFormatter by lazy {
        DateTimeRangeFormatter(
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