package epicarchitect.breakbadhabits

import android.app.Application
import epicarchitect.breakbadhabits.di.declaration.main.AppModule
import epicarchitect.breakbadhabits.di.declaration.main.FormatModuleExternals
import epicarchitect.breakbadhabits.di.declaration.main.MainDatabaseModuleExternals
import epicarchitect.breakbadhabits.di.holder.AppModuleHolder
import epicarchitect.breakbadhabits.foundation.sqldelight.SqlDriverFactory
import epicarchitect.breakbadhabits.ui.format.DurationFormatter
import epicarchitect.breakbadhabits.ui.format.android.AndroidDateTimeFormatter
import epicarchitect.breakbadhabits.ui.format.android.AndroidDurationFormatter

fun setupAppModuleHolder(app: Application) {
    AppModuleHolder.current = AppModule(
        formatExternals = object : FormatModuleExternals {
            override val dateTimeFormatter by lazy {
                AndroidDateTimeFormatter(
//                    dateTimeProvider = AppModuleHolder.require().dateTime.dateTimeProvider,
                    context = app
                )
            }
            override val durationFormatter = AndroidDurationFormatter(
                resources = app.resources,
                defaultAccuracy = DurationFormatter.Accuracy.SECONDS
            )
        },
        mainDatabaseExternals = object : MainDatabaseModuleExternals {
            override val sqlDriverFactory = SqlDriverFactory(app)
        },
//        habitsLogicExternals = object : HabitsLogicModuleExternals {
//            override val habitIcons = HabitIcons()
//        }
    )
}