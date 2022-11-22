package breakbadhabits.app.dependencies.main

import android.app.Application
import androidx.room.Room
import breakbadhabits.app.dependencies.AppDependencies
import breakbadhabits.app.dependencies.main.database.MainDatabase
import breakbadhabits.app.dependencies.main.repository.HabitTracksRepositoryImpl
import breakbadhabits.app.dependencies.main.repository.HabitsRepositoryImpl
import breakbadhabits.app.dependencies.main.repository.IdGenerator
import breakbadhabits.app.dependencies.main.utils.TimeProviderImpl
import breakbadhabits.feature.habits.di.HabitsFeatureFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class MainAppDependencies(private val application: Application) : AppDependencies {
    private val idGenerator by lazy {
        IdGenerator(application)
    }

    private val database by lazy {
        Room.databaseBuilder(
            application,
            MainDatabase::class.java,
            "database-v4"
        ).build()
    }

    private val habitsRepository by lazy {
        HabitsRepositoryImpl(
            idGenerator,
            database.habitsDao
        )
    }

    private val habitTracksRepository by lazy {
        HabitTracksRepositoryImpl(
            idGenerator,
            database.habitTracksDao
        )
    }

    private val timeProvider by lazy {
        TimeProviderImpl(updatePeriodMillis = 1000)
    }

    override val habitsFeatureFactory by lazy {
        HabitsFeatureFactory(
            habitsRepository = habitsRepository,
            habitTracksRepository = habitTracksRepository,
            timeProvider = timeProvider,
            maxHabitNameLength = 30
        )
    }
}