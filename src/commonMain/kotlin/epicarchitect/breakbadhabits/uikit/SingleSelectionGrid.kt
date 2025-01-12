package epicarchitect.breakbadhabits.uikit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.ceil

@Composable
fun <T> SingleSelectionGrid(
    modifier: Modifier = Modifier,
    items: List<T>,
    cell: @Composable BoxScope.(T) -> Unit,
    selectedItem: T?,
    onSelect: (T) -> Unit,
    countInRow: Int = 7
) {
    val countRows = ceil((items.size / countInRow.toFloat())).toInt()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(countRows) { rowIndex ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(countInRow) { itemIndex ->
                    val item = items.getOrNull(rowIndex * 7 + itemIndex)
                    if (item != null) {
                        val isSelected = item == selectedItem

                        androidx.compose.material3.Card(
                            modifier = Modifier.size(44.dp),
                            shape = MaterialTheme.shapes.small,
                            onClick = {
                                onSelect(item)
                            },
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    Color.Unspecified
                                }
                            ),
                            elevation = CardDefaults.cardElevation(0.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                cell(item)
                            }
                        }
                    }
                }
            }
        }
    }
}