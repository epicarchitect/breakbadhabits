package epicarchitect.breakbadhabits.screens.habits.details

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.screens.root.LocalRootNavController
import epicarchitect.breakbadhabits.screens.root.RootRoute
import epicarchitect.breakbadhabits.uikit.SimpleScrollableScreen

@Composable
fun HabitDetailsScreen(habitId: Int) {
    val state = rememberHabitDetailsScreenState(habitId) ?: return
    val density = LocalDensity.current
    val navController = LocalRootNavController.current

    val scrollState = rememberScrollState()
    val showNameInAppBar by remember {
        derivedStateOf {
            with(density) {
                scrollState.value.toDp() > 80.dp
            }
        }
    }

    SimpleScrollableScreen(
        title = if (showNameInAppBar) state.habit.name else "",
        scrollState = scrollState,
        onBackClick = navController::popBackStack,
        actions = {
            IconButton(
                onClick = {
                    navController.navigate(RootRoute.HabitEditing(habitId))
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = null
                )
            }
        }
    ) {
        Spacer(Modifier.height(16.dp))

        HabitDetailsHeaderSection(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            state = state
        )

        if (state.habitEventRecords.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))
            HabitDetailsCalendarCard(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                state = state
            )
        }

        if (state.abstinenceHistogramValues.size > 2) {
            Spacer(Modifier.height(16.dp))
            HabitDetailsHistogramCard(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                state = state
            )
        }

        if (state.statistics.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))
            HabitDetailsStatisticsCard(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                state = state
            )
        }

        Spacer(Modifier.height(16.dp))
    }
}