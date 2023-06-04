package epicarchitect.breakbadhabits.android.app

import android.app.Application
import epicarchitect.breakbadhabits.android.app.icons.HabitIconProvider
import epicarchitect.breakbadhabits.database.main.MainDatabaseFactory
import epicarchitect.breakbadhabits.di.declaration.impl.AppModuleImpl
import epicarchitect.breakbadhabits.di.declaration.impl.database.MainDatabaseModuleExternal
import epicarchitect.breakbadhabits.di.declaration.impl.foundation.FoundationModuleExternal
import epicarchitect.breakbadhabits.di.declaration.impl.foundation.FoundationModuleImpl
import epicarchitect.breakbadhabits.di.declaration.impl.foundation.IdentificationModuleExternal
import epicarchitect.breakbadhabits.di.declaration.impl.logic.HabitsLogicModuleExternal
import epicarchitect.breakbadhabits.di.declaration.impl.logic.LogicModuleExternal
import epicarchitect.breakbadhabits.di.declaration.impl.logic.LogicModuleImpl
import epicarchitect.breakbadhabits.di.declaration.impl.presentation.PresentationModuleImpl
import epicarchitect.breakbadhabits.di.declaration.impl.ui.FormatModuleExternal
import epicarchitect.breakbadhabits.di.declaration.impl.ui.UiModuleExternal
import epicarchitect.breakbadhabits.di.declaration.impl.ui.UiModuleImpl
import epicarchitect.breakbadhabits.di.holder.AppModuleHolder
import epicarchitect.breakbadhabits.foundation.identification.android.AndroidIdGenerator
import epicarchitect.breakbadhabits.habits.widget.android.HabitsAppWidgetProvider
import epicarchitect.breakbadhabits.ui.format.DurationFormatter
import epicarchitect.breakbadhabits.ui.format.android.AndroidDateTimeFormatter
import epicarchitect.breakbadhabits.ui.format.android.AndroidDurationFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn

private const val MAIN_DATABASE_NAME = "breakbadhabits-main" // TODO make some config

class BreakBadHabitsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        setupAppModuleHolder(this)
        registerAppWidgetUpdates(this)
    }
}

private fun registerAppWidgetUpdates(app: Application) {
    combine(
        AppModuleHolder.logic.habits.habitProvider.habitsFlow(),
        AppModuleHolder.logic.habits.habitTrackProvider.habitTracksFlow(),
        AppModuleHolder.logic.habits.habitWidgetProvider.provideAllFlow()
    ) { _, _, _ ->
        HabitsAppWidgetProvider.sendUpdateIntent(app)
    }.launchIn(CoroutineScope(Dispatchers.Default))
}

fun setupAppModuleHolder(app: Application) {
    val foundationModule = FoundationModuleImpl(
        external = object : FoundationModuleExternal {
            override val identificationModuleExternal = object : IdentificationModuleExternal {
                override val idGenerator = AndroidIdGenerator(app)
            }
        }
    )
    val logicModule = LogicModuleImpl(
        foundation = foundationModule,
        external = object : LogicModuleExternal {
            override val mainDatabaseExternal = object : MainDatabaseModuleExternal {
                override val mainDatabase = MainDatabaseFactory.create(
                    context = app,
                    name = MAIN_DATABASE_NAME
                )
            }
            override val habitsExternal = object : HabitsLogicModuleExternal {
                override val habitIconProvider = HabitIconProvider()
            }
        }
    )
    val presentationModule = PresentationModuleImpl(
        logic = logicModule
    )
    val uiModule = UiModuleImpl(
        presentation = presentationModule,
        external = object : UiModuleExternal {
            override val formatModuleExternal = object : FormatModuleExternal {
                override val dateTimeFormatter = AndroidDateTimeFormatter(
                    dateTimeProvider = logicModule.dateTime.dateTimeProvider,
                    context = app
                )
                override val durationFormatter = AndroidDurationFormatter(
                    resources = app.resources,
                    defaultAccuracy = DurationFormatter.Accuracy.SECONDS
                )
            }
        }
    )

    AppModuleHolder.current = AppModuleImpl(uiModule)
}
