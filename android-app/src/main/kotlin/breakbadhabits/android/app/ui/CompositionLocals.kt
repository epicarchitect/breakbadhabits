package breakbadhabits.android.app.ui

import androidx.compose.runtime.compositionLocalOf
import breakbadhabits.android.app.ui.habits.resources.HabitIconResourceProvider
import breakbadhabits.app.di.PresentationModule

val LocalPresentationModule = compositionLocalOf<PresentationModule> {
    error("LocalPresentationModule not provided")
}

val LocalHabitIconResourceProvider = compositionLocalOf<HabitIconResourceProvider> {
    error("LocalHabitIconResources not provided")
}

val LocalDateTimeFormatter = compositionLocalOf<DateTimeFormatter> {
    error("LocalDateTimeFormatter not provided")
}