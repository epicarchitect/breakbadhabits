package epicarchitect.breakbadhabits.uikit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AppIconTile(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    selected: Boolean = false
) {
    val scheme = MaterialTheme.colorScheme
    Box(
        modifier = modifier
            .size(48.dp)
            .then(
                Modifier
                    .background(
                        color = if (selected) {
                            scheme.primary.copy(alpha = 0.24f)
                        } else {
                            scheme.primary.copy(alpha = 0.14f)
                        },
                        shape = MaterialTheme.shapes.medium
                    )
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = imageVector,
            contentDescription = null,
            tint = if (selected) scheme.primary else scheme.primary.copy(alpha = 0.82f)
        )
    }
}

@Preview(widthDp = 80, heightDp = 80, name = "AppIconTile")
@Composable
private fun AppIconTilePreview() {
    PreviewTheme {
        AppIconTile(
            imageVector = Icons.Filled.Star
        )
    }
}
