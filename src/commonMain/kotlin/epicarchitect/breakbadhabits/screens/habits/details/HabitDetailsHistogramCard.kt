package epicarchitect.breakbadhabits.screens.habits.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.environment.LocalAppEnvironment
import epicarchitect.breakbadhabits.format.DurationFormatter
import epicarchitect.breakbadhabits.uikit.Histogram
import kotlin.time.Duration.Companion.seconds

@Composable
fun HabitDetailsHistogramCard(
    modifier: Modifier = Modifier,
    state: HabitDetailsScreenState
) {
    val environment = LocalAppEnvironment.current
    val durationFormatter = environment.format.durationFormatter
    val strings = environment.resources.strings.habitDashboardStrings

    Card(modifier) {
        Column {
            Text(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                text = strings.abstinenceChartTitle(),
                style = MaterialTheme.typography.titleMedium
            )

            Histogram(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                values = state.abstinenceHistogramValues,
                valueFormatter = {
                    durationFormatter.format(
                        duration = it.toInt().seconds,
                        accuracy = DurationFormatter.Accuracy.DAYS
                    )
                }
            )
        }
    }
}