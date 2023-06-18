package epicarchitect.breakbadhabits

import epicarchitect.breakbadhabits.database.DriverFactory
import epicarchitect.breakbadhabits.database.MainDatabaseFactory
import epicarchitect.breakbadhabits.di.declaration.main.AppModule
import epicarchitect.breakbadhabits.di.declaration.main.database.MainDatabaseModuleExternals
import epicarchitect.breakbadhabits.di.declaration.main.foundation.FoundationModule
import epicarchitect.breakbadhabits.di.declaration.main.logic.HabitsLogicModuleExternals
import epicarchitect.breakbadhabits.di.declaration.main.logic.LogicModule
import epicarchitect.breakbadhabits.di.declaration.main.logic.LogicModuleExternals
import epicarchitect.breakbadhabits.di.declaration.main.presentation.PresentationModule
import epicarchitect.breakbadhabits.di.declaration.main.ui.FormatModuleExternals
import epicarchitect.breakbadhabits.di.declaration.main.ui.UiModule
import epicarchitect.breakbadhabits.di.declaration.main.ui.UiModuleExternals
import epicarchitect.breakbadhabits.di.holder.AppModuleHolder
import epicarchitect.breakbadhabits.ui.format.DateTimeFormatter
import epicarchitect.breakbadhabits.ui.format.DurationFormatter
import epicarchitect.breakbadhabits.ui.format.ios.IosDateTimeFormatter
import epicarchitect.breakbadhabits.ui.format.ios.IosDurationFormatter
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlin.time.Duration

@Suppress("unused")
fun setupAppModuleHolder() {
    val logicModule = LogicModule(
        foundation = FoundationModule(),
        externals = object : LogicModuleExternals {
            override val mainDatabaseExternal = object : MainDatabaseModuleExternals {
                override val mainDatabase = MainDatabaseFactory(DriverFactory()).create()
            }
            override val habitsExternal = object : HabitsLogicModuleExternals {
                override val habitIconProvider = HabitIconProvider()
            }
        }
    )
    val uiModule = UiModule(
        presentation = PresentationModule(
            logic = logicModule
        ),
        externals = object : UiModuleExternals {
            override val formatModuleExternal = object : FormatModuleExternals {
                override val dateTimeFormatter = IosDateTimeFormatter()
                override val durationFormatter = IosDurationFormatter()
            }
        }
    )

    AppModuleHolder.current = AppModule(uiModule)
}
