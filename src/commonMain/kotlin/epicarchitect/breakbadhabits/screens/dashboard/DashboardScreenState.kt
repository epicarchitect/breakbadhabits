package epicarchitect.breakbadhabits.screens.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.environment.LocalAppEnvironment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Immutable
data class DashboardScreenState(
    val habits: List<Habit>
)

@Composable
fun rememberDashboardScreenState(): DashboardScreenState? {
    val environment = LocalAppEnvironment.current
    val habitQueries = environment.database.habitQueries
    val habitState = remember {
        habitQueries.habits().asFlow().mapToList(Dispatchers.IO)
    }.collectAsState(null)

    val habits = habitState.value

    return remember(habits) {
        if (habits == null) null
        else DashboardScreenState(habits)
    }
}