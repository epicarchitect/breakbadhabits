package epicarchitect.breakbadhabits.screens.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import epicarchitect.breakbadhabits.database.HabitWithLastEvent
import epicarchitect.breakbadhabits.infrastructure.AppDatabase

@Immutable
data class DashboardScreenState(
    val habits: List<HabitWithLastEvent>
)

@Composable
fun rememberDashboardScreenState(): DashboardScreenState? {
    val habits by remember { AppDatabase.habitsDao.habitsWithLastEvent() }.collectAsState(null)

    return remember(habits) {
        habits?.let(::DashboardScreenState)
    }
}
