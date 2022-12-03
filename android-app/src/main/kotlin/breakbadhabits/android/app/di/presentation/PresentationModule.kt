package breakbadhabits.android.app.di.presentation

import android.app.Application
import breakbadhabits.android.app.di.logic.LogicModule
import breakbadhabits.presentation.CurrentHabitAbstinenceModule
import breakbadhabits.presentation.HabitCreationModule
import breakbadhabits.presentation.HabitIdsModule
import breakbadhabits.presentation.HabitModule

class PresentationModule(private val application: Application) {
    private val logicModule by lazy {
        LogicModule(application)
    }

    val currentHabitAbstinenceModule by lazy {
        CurrentHabitAbstinenceModule(logicModule.currentHabitAbstinenceProviderModule)
    }

    val habitCreationModule by lazy {
        HabitCreationModule(
            logicModule.habitCreatorModule,
            logicModule.habitIconsProviderModule
        )
    }

    val habitModule by lazy {
        HabitModule(logicModule.habitProviderModule)
    }

    val habitIdsModule by lazy {
        HabitIdsModule(logicModule.habitIdsProviderModule)
    }
}