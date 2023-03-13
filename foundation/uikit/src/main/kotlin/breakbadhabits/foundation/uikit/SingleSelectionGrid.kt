package breakbadhabits.foundation.uikit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import breakbadhabits.foundation.controller.SingleSelectionController
import breakbadhabits.foundation.uikit.ext.collectState
import kotlin.math.ceil

@Composable
fun <T> SingleSelectionGrid(
    modifier: Modifier = Modifier,
    controller: SingleSelectionController<T>,
    ceil: @Composable BoxScope.(T) -> Unit,
    countInRow: Int = 7
) {
    val state by controller.collectState()

    val countRows = ceil((state.items.size / countInRow.toFloat())).toInt()

    Column(modifier = modifier) {
        repeat(countRows) { rowIndex ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(countInRow) { itemIndex ->
                    val item = state.items.getOrNull(rowIndex * 7 + itemIndex)
                    if (item != null) {
                        CompositionLocalProvider(
                            LocalContentColor provides if (item == state.selectedItem) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(MaterialTheme.shapes.small)
                                    .clickable {
                                        controller.select(item)
                                    }
                            ) {
                                ceil(item)
                            }
                        }
                    }
                }
            }
        }
    }
}

