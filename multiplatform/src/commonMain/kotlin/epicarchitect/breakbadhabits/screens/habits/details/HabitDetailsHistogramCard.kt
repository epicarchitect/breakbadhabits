package epicarchitect.breakbadhabits.screens.habits.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.format.Accuracy
import epicarchitect.breakbadhabits.format.formatted
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.abstinence_chart_title
import epicarchitect.breakbadhabits.uikit.Histogram
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration.Companion.seconds

@Composable
fun HabitDetailsHistogramCard(
    modifier: Modifier = Modifier,
    state: HabitDetailsScreenState
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column {
            Text(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                text = stringResource(Res.string.abstinence_chart_title),
                style = MaterialTheme.typography.titleMedium
            )

            Histogram(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                values = state.abstinenceHistogramValues,
                valueFormatter = {
                    it.toInt().seconds.formatted(Accuracy.DAYS)
                }
            )
        }
    }
}