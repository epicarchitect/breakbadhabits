package epicarchitect.breakbadhabits.foundation.uikit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.foundation.uikit.text.Text

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
                    type = Text.Type.Description,
                    priority = Text.Priority.Medium
                )
                Text(
                    text = item.value,
                    type = Text.Type.Label,
                    priority = Text.Priority.High
                )
            }

            if (item != statistics.last()) {
                Divider()
            }
        }
    }
}