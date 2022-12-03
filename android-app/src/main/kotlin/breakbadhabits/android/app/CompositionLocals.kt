package breakbadhabits.android.app

import androidx.compose.runtime.compositionLocalOf
import breakbadhabits.android.app.di.presentation.PresentationModule
import breakbadhabits.android.app.format.HabitAbstinenceFormatter
import breakbadhabits.app.ui.resources.HabitIconResources

val LocalPresentationModule = compositionLocalOf<PresentationModule> {
    error("LocalPresentationModule not provided")
}

val LocalHabitIconResources = compositionLocalOf<HabitIconResources> {
    error("LocalHabitIconResources not provided")
}

val LocalHabitAbstinenceFormatter = compositionLocalOf<HabitAbstinenceFormatter> {
    error("LocalHabitAbstinenceFormatter not provided")
}