package epicarchitect.breakbadhabits.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import epicarchitect.breakbadhabits.ui.component.theme.AppTheme

@Composable
fun SingleSelectionChipRow(
    items: List<String>,
    onClick: (index: Int) -> Unit,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    edgePadding: Dp = 12.dp
) {
    ScrollableTabRow(
        modifier = modifier,
        selectedTabIndex = selectedIndex,
        containerColor = Color.Transparent,
        indicator = {},
        divider = {},
        edgePadding = edgePadding
    ) {
        items.forEachIndexed { index, item ->
            val isSelected = selectedIndex == index
            Card(
                modifier = Modifier.padding(4.dp),
                backgroundColor = if (isSelected) {
                    AppTheme.colorScheme.primary
                } else {
                    AppTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                },
                elevation = 0.dp
            ) {
                Text(
                    modifier = Modifier
                        .defaultMinSize(minWidth = 90.dp)
                        .clickable {
                            onClick(index)
                        }
                        .padding(
                            vertical = 8.dp,
                            horizontal = 20.dp
                        ),
                    text = item,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp,
                    color = if (isSelected) {
                        AppTheme.colorScheme.onPrimary
                    } else {
                        AppTheme.colorScheme.onBackground
                    }
                )
            }
        }
    }
}