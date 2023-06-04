package epicarchitect.breakbadhabits

import android.app.Application
import epicarchitect.breakbadhabits.database.DriverFactory
import epicarchitect.breakbadhabits.database.MainDatabaseFactory
import epicarchitect.breakbadhabits.di.declaration.impl.AppModuleImpl
import epicarchitect.breakbadhabits.di.declaration.impl.database.MainDatabaseModuleExternal
import epicarchitect.breakbadhabits.di.declaration.impl.foundation.CoroutinesModuleExternal
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
import epicarchitect.breakbadhabits.foundation.coroutines.AndroidCoroutineDispatchers
import epicarchitect.breakbadhabits.foundation.identification.android.AndroidIdGenerator
import epicarchitect.breakbadhabits.ui.format.DurationFormatter
import epicarchitect.breakbadhabits.ui.format.android.AndroidDateTimeFormatter
import epicarchitect.breakbadhabits.ui.format.android.AndroidDurationFormatter

fun setupAppModuleHolder(app: Application) {
    val foundationModule = FoundationModuleImpl(
        external = object : FoundationModuleExternal {
            override val identificationModuleExternal = object : IdentificationModuleExternal {
                override val idGenerator = AndroidIdGenerator(app)
            }
            override val coroutinesModuleExternal = object : CoroutinesModuleExternal {
                override val coroutineDispatchers = AndroidCoroutineDispatchers()
            }
        }
    )
    val logicModule = LogicModuleImpl(
        foundation = foundationModule,
        external = object : LogicModuleExternal {
            override val mainDatabaseExternal = object : MainDatabaseModuleExternal {
                override val mainDatabase = MainDatabaseFactory.create(DriverFactory(app))
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