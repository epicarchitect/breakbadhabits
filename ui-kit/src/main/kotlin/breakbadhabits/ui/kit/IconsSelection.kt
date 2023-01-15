package breakbadhabits.ui.kit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlin.math.ceil

data class IconData(
    val id: Long,
    val resourceId: Int
)

@Composable
fun IconsSelection(
    modifier: Modifier = Modifier,
    icons: List<IconData>,
    selectedIcon: IconData,
    onSelect: (icon: IconData) -> Unit,
    countInRow: Int = 7
) {
    val countRows = ceil((icons.size / countInRow.toFloat())).toInt()

    Column(modifier = modifier) {
        repeat(countRows) { rowIndex ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(countInRow) { itemIndex ->
                    val icon = icons.getOrNull(rowIndex * 7 + itemIndex)

                    if (icon != null) {
                        Box(
                            modifier = Modifier.size(44.dp)
                        ) {
                            IconButton(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(24.dp),
                                onClick = {
                                    onSelect(icon)
                                }
                            ) {
                                Icon(
                                    modifier = Modifier.fillMaxSize(),
                                    painter = painterResource(icon.resourceId),
                                    tint = if (selectedIcon.id == icon.id) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.onSurface
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

