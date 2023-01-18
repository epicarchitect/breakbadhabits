package breakbadhabits.android.app

import androidx.compose.runtime.compositionLocalOf
import breakbadhabits.android.app.resources.HabitIconResources
import breakbadhabits.app.dependecies.PresentationModule

val LocalPresentationModule = compositionLocalOf<PresentationModule> {
    error("LocalPresentationModule not provided")
}

val LocalHabitIconResources = compositionLocalOf<HabitIconResources> {
    error("LocalHabitIconResources not provided")
}