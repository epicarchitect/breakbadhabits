package breakbadhabits.android.app.di.logic

import android.app.Application
import androidx.room.Room
import breakbadhabits.android.app.database.MainDatabase
import breakbadhabits.android.app.repository.HabitIconRepository
import breakbadhabits.android.app.repository.HabitTracksRepository
import breakbadhabits.android.app.repository.HabitsRepository
import breakbadhabits.android.app.repository.IdGenerator
import breakbadhabits.android.app.utils.TimeProvider
import breakbadhabits.logic.CurrentHabitAbstinenceProviderModule
import breakbadhabits.logic.HabitCreatorModule
import breakbadhabits.logic.HabitIconsProviderModule
import breakbadhabits.logic.HabitIdsProviderModule
import breakbadhabits.logic.HabitProviderModule

class LogicModule(private val application: Application) {

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
        HabitsRepository(
            idGenerator,
            database.habitsDao
        )
    }

    private val habitTracksRepository by lazy {
        HabitTracksRepository(
            idGenerator,
            database.habitTracksDao
        )
    }

    private val timeProvider by lazy {
        TimeProvider(1000)
    }

    private val habitIconRepository by lazy {
        HabitIconRepository()
    }

    val currentHabitAbstinenceProviderModule by lazy {
        CurrentHabitAbstinenceProviderModule(
            CurrentHabitAbstinenceProviderModuleDelegate(
                habitTracksRepository,
                timeProvider
            )
        )
    }

    val habitCreatorModule by lazy {
        HabitCreatorModule(
            HabitCreatorModuleDelegate(
                habitsRepository,
                habitTracksRepository,
                timeProvider
            )
        )
    }

    val habitIconsProviderModule by lazy {
        HabitIconsProviderModule(
            HabitIconProviderModuleDelegate(
                habitIconRepository
            )
        )
    }

    val habitIdsProviderModule by lazy {
        HabitIdsProviderModule(
            HabitIdsProviderModuleDelegate(
                habitsRepository
            )
        )
    }

    val habitProviderModule by lazy {
        HabitProviderModule(
            HabitProviderModuleDelegate(
                habitsRepository
            )
        )
    }
}