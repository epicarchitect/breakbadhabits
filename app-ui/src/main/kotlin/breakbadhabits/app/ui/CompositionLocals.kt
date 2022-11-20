package breakbadhabits.app.ui

import androidx.compose.runtime.compositionLocalOf
import breakbadhabits.app.dependencies.AppDependencies
import breakbadhabits.app.ui.resources.HabitIconResources

val LocalAppDependencies = compositionLocalOf<AppDependencies> {
    error("LocalAppDependencies not provided")
}

val LocalHabitIcons = compositionLocalOf<HabitIconResources> {
    error("LocalHabitIcons not provided")
}