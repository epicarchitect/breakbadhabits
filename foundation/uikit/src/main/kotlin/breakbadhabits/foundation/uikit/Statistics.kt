package breakbadhabits.foundation.uikit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import breakbadhabits.foundation.uikit.text.Text

data class StatisticData(
    val name: String,
    val value: String
)

@Composable
fun Statistics(
    modifier: Modifier = Modifier,
    statistics: List<StatisticData>,
    verticalItemPadding: Dp = 8.dp,
    horizontalItemPadding: Dp = 16.dp,
) {
    Column(
        modifier = modifier
    ) {
        statistics.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = verticalItemPadding,
                        horizontal = horizontalItemPadding
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(item.name)

                Spacer(modifier = Modifier.weight(1f))

                Text(item.value)
            }

            if (item != statistics.last()) {
                Divider(modifier = Modifier.padding(horizontal = horizontalItemPadding))
            }
        }
    }
}