package breakbadhabits.ui.kit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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
    nameTextFraction: Float = 0.6f
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
                Text(
                    modifier = Modifier.fillMaxWidth(nameTextFraction),
                    text = item.name
                )
                Text(
                    text = item.value
                )
            }

            if (item != statistics.last()) {
                Divider(modifier = Modifier.padding(horizontal = horizontalItemPadding))
            }
        }
    }
}