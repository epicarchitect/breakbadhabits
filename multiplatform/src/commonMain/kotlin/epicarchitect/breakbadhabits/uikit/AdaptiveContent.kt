package epicarchitect.breakbadhabits.uikit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class AdaptiveContentWidth(val maxWidth: Dp) {
    Reading(600.dp),
    Wide(840.dp)
}

@Composable
fun AdaptiveContent(
    modifier: Modifier = Modifier,
    width: AdaptiveContentWidth = AdaptiveContentWidth.Reading,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = width.maxWidth)
                .fillMaxWidth(),
            content = content
        )
    }
}

@Preview(widthDp = 360, heightDp = 240, name = "AdaptiveContent compact")
@Preview(widthDp = 900, heightDp = 240, name = "AdaptiveContent wide")
@Composable
private fun AdaptiveContentPreview() {
    PreviewTheme {
        AdaptiveContent(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Content",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}
