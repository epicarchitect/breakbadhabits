package breakbadhabits.app.ui

import androidx.compose.runtime.compositionLocalOf
import breakbadhabits.app.dependencies.AppDependencies
import breakbadhabits.app.ui.resources.HabitIconResources

val LocalAppDependencies = compositionLocalOf<AppDependencies> {
    error("LocalAppDependencies not provided")
}

val LocalHabitIconResources = compositionLocalOf<HabitIconResources> {
    error("LocalHabitIconResources not provided")
}

val LocalHabitAbstinenceFormatter = compositionLocalOf<HabitAbstinenceFormatter> {
    error("LocalHabitAbstinenceFormatter not provided")
}