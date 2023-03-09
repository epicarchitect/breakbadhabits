package breakbadhabits.android.app.ui

import androidx.compose.runtime.compositionLocalOf
import breakbadhabits.android.app.ui.habits.resources.HabitIconResources
import breakbadhabits.app.di.PresentationModule

val LocalPresentationModule = compositionLocalOf<PresentationModule> {
    error("LocalPresentationModule not provided")
}

val LocalHabitIconResources = compositionLocalOf<HabitIconResources> {
    error("LocalHabitIconResources not provided")
}