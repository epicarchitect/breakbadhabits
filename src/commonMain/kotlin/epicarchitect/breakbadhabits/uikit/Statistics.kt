package epicarchitect.breakbadhabits.uikit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class StatisticData(
    val name: String,
    val value: String
)

@Composable
fun Statistics(
    modifier: Modifier = Modifier,
    statistics: List<StatisticData>
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        statistics.forEach { item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .weight(1f),
                    text = item.name,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = item.value,
                    style = MaterialTheme.typography.labelLarge
                )
            }

            if (item != statistics.last()) {
                HorizontalDivider()
            }
        }
    }
}