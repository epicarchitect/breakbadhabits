package epicarchitect.breakbadhabits.screens.dashboard

import androidx.compose.runtime.compositionLocalOf

val LocalDashboardResources = compositionLocalOf<DashboardResources> {
    error("LocalDashboardResources not provided")
}

interface DashboardResources {
    val titleText: String
    val newHabitButtonText: String
    val emptyHabitsText: String
    val habitHasNoEvents: String
}