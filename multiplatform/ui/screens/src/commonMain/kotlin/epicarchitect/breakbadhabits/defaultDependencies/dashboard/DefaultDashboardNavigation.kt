package epicarchitect.breakbadhabits.defaultDependencies.dashboard

import cafe.adriel.voyager.navigator.Navigator
import epicarchitect.breakbadhabits.defaultDependencies.appSettings.AppSettingsScreen
import epicarchitect.breakbadhabits.defaultDependencies.habits.creation.HabitCreationScreen
import epicarchitect.breakbadhabits.defaultDependencies.habits.details.HabitDetailsScreen
import epicarchitect.breakbadhabits.defaultDependencies.habits.tracks.creation.HabitTrackCreationScreen
import epicarchitect.breakbadhabits.features.dashboard.DashboardNavigation

class DefaultDashboardNavigation(private val navigator: Navigator) : DashboardNavigation {
    override fun openAppSettings() {
        navigator += AppSettingsScreen()
    }

    override fun openHabitCreation() {
        navigator += HabitCreationScreen()
    }

    override fun openHabitDetails(habitId: Int) {
        navigator += HabitDetailsScreen(habitId)
    }

    override fun openHabitTrackCreation(habitId: Int) {
        navigator += HabitTrackCreationScreen(habitId)
    }
}