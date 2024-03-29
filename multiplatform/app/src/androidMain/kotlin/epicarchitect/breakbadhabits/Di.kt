package epicarchitect.breakbadhabits

import android.app.Application
import epicarchitect.breakbadhabits.di.declaration.main.AppModule
import epicarchitect.breakbadhabits.di.declaration.main.database.MainDatabaseModuleExternals
import epicarchitect.breakbadhabits.di.declaration.main.foundation.FoundationModule
import epicarchitect.breakbadhabits.di.declaration.main.logic.HabitsLogicModuleExternals
import epicarchitect.breakbadhabits.di.declaration.main.logic.LogicModule
import epicarchitect.breakbadhabits.di.declaration.main.logic.LogicModuleExternals
import epicarchitect.breakbadhabits.di.declaration.main.ui.FormatModuleExternals
import epicarchitect.breakbadhabits.di.declaration.main.ui.UiModule
import epicarchitect.breakbadhabits.di.declaration.main.ui.UiModuleExternals
import epicarchitect.breakbadhabits.di.holder.AppModuleHolder
import epicarchitect.breakbadhabits.foundation.sqldelight.SqlDriverFactory
import epicarchitect.breakbadhabits.screens.HabitIconProvider
import epicarchitect.breakbadhabits.ui.format.DurationFormatter
import epicarchitect.breakbadhabits.ui.format.android.AndroidDateTimeFormatter
import epicarchitect.breakbadhabits.ui.format.android.AndroidDurationFormatter

fun setupAppModuleHolder(app: Application) {
    val logicModule = LogicModule(
        foundation = FoundationModule(),
        externals = object : LogicModuleExternals {
            override val mainDatabaseExternal = object : MainDatabaseModuleExternals {
                override val sqlDriverFactory = SqlDriverFactory(app)
            }
            override val habitsExternal = object : HabitsLogicModuleExternals {
                override val habitIconProvider = HabitIconProvider()
            }
        }
    )
    val uiModule = UiModule(
        externals = object : UiModuleExternals {
            override val formatModuleExternal = object : FormatModuleExternals {
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

    AppModuleHolder.current = AppModule(uiModule, logicModule)
}