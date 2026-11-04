package epicarchitect.breakbadhabits.uikit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private val SelectionCellMinSize = 44.dp
private val SelectionCellSpacing = 8.dp

@Composable
fun <T> SingleSelectionGrid(
    modifier: Modifier = Modifier,
    items: List<T>,
    cell: @Composable BoxScope.(T) -> Unit,
    selectedItem: T?,
    onSelect: (T) -> Unit,
    maxItemsInRow: Int = 10
) {
    BoxWithConstraints(modifier = modifier) {
        val itemsInRow = ((maxWidth + SelectionCellSpacing) /
                (SelectionCellMinSize + SelectionCellSpacing))
            .toInt()
            .coerceIn(1, maxItemsInRow)

        Column(
            verticalArrangement = Arrangement.spacedBy(SelectionCellSpacing)
        ) {
            items.chunked(itemsInRow).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(SelectionCellSpacing)
                ) {
                    rowItems.forEach { item ->
                        val isSelected = item == selectedItem

                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
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
                    repeat(itemsInRow - rowItems.size) {
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                        )
                    }
                }
            }
        }
    }
}

@Preview(widthDp = 360, heightDp = 260, name = "SingleSelectionGrid compact")
@Composable
private fun SingleSelectionGridCompactPreview() {
    PreviewTheme {
        SingleSelectionGrid(
            modifier = Modifier.fillMaxWidth(),
            items = (1..20).toList(),
            selectedItem = 4,
            onSelect = {},
            cell = { item ->
                Text(
                    text = item.toString(),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        )
    }
}

@Preview(widthDp = 900, heightDp = 260, name = "SingleSelectionGrid wide")
@Composable
private fun SingleSelectionGridWidePreview() {
    PreviewTheme {
        SingleSelectionGrid(
            modifier = Modifier.fillMaxWidth(),
            items = (1..20).toList(),
            selectedItem = 4,
            onSelect = {},
            cell = { item ->
                Text(
                    text = item.toString(),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        )
    }
}
