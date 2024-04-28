package epicarchitect.breakbadhabits.features.dashboard

interface DashboardNavigation {
    fun openAppSettings()
    fun openHabitCreation()
    fun openHabitDetails(habitId: Int)
    fun openHabitTrackCreation(habitId: Int)
}