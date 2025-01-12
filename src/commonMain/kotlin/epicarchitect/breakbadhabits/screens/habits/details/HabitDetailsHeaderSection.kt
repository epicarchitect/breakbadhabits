package epicarchitect.breakbadhabits.screens.habits.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.environment.LocalAppEnvironment
import epicarchitect.breakbadhabits.format.DurationFormatter
import epicarchitect.breakbadhabits.screens.root.LocalRootNavController
import epicarchitect.breakbadhabits.screens.root.RootRoute

@Composable
fun HabitDetailsHeaderSection(
    modifier: Modifier = Modifier,
    state: HabitDetailsScreenState
) {
    val environment = LocalAppEnvironment.current
    val navController = LocalRootNavController.current
    val durationFormatter = environment.format.durationFormatter
    val strings = environment.resources.strings.habitDashboardStrings
    val habitIcons = environment.habits.icons

    Column(modifier) {
        Icon(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(44.dp),
            imageVector = habitIcons.getById(state.habit.iconId).imageVector,
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = state.habit.name,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            text = state.abstinence?.let {
                durationFormatter.format(
                    duration = it,
                    accuracy = DurationFormatter.Accuracy.SECONDS
                )
            } ?: strings.habitHasNoEvents(),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                navController.navigate(
                    RootRoute.HabitEventRecordEditing(
                        habitEventRecordId = null,
                        habitId = state.habit.id
                    )
                )
            }
        ) {
            Text(
                text = strings.addHabitEventRecord()
            )
        }
    }
}