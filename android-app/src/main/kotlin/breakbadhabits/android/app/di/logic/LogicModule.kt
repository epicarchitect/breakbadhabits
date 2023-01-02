package breakbadhabits.android.app.di.logic

import android.content.Context
import androidx.room.Room
import breakbadhabits.android.app.database.MainDatabase
import breakbadhabits.android.app.repository.HabitIconRepository
import breakbadhabits.android.app.repository.HabitTracksRepository
import breakbadhabits.android.app.repository.HabitsRepository
import breakbadhabits.android.app.repository.IdGenerator
import breakbadhabits.android.app.utils.TimeProvider
import breakbadhabits.logic.CurrentHabitAbstinenceProviderModule
import breakbadhabits.logic.HabitCreatorModule
import breakbadhabits.logic.HabitDeleterModule
import breakbadhabits.logic.HabitIconsProviderModule
import breakbadhabits.logic.HabitIdsProviderModule
import breakbadhabits.logic.HabitProviderModule
import breakbadhabits.logic.HabitTrackCreatorModule
import breakbadhabits.logic.HabitTrackProviderModule

class LogicModule(private val context: Context) {

    private val idGenerator by lazy {
        IdGenerator(context)
    }

    private val database by lazy {
        Room.databaseBuilder(
            context,
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

    val habitDeleterModule by lazy {
        HabitDeleterModule(
            HabitDeleterModuleDelegate(
                habitsRepository,
                habitTracksRepository
            )
        )
    }

    val habitTrackCreatorModule by lazy {
        HabitTrackCreatorModule(
            HabitTrackCreatorModuleDelegate(
                habitTracksRepository
            )
        )
    }

    val habitTrackProviderModule by lazy {
        HabitTrackProviderModule(
            HabitTrackProviderModuleDelegate(
                habitTracksRepository
            )
        )
    }
}