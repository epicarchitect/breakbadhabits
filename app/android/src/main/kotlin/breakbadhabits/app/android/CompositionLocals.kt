package breakbadhabits.app.android

import androidx.compose.runtime.compositionLocalOf
import breakbadhabits.app.di.PresentationModule

val LocalPresentationModule = compositionLocalOf<PresentationModule> {
    error("LocalPresentationModule not provided")
}

val LocalHabitIconResources = compositionLocalOf<HabitIconResources> {
    error("LocalHabitIconResources not provided")
}