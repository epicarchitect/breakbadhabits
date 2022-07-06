package breakbadhabits.android.compose.molecule

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ErrorText(
    text: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        color = MaterialTheme.colors.error,
        text = text,
        imageVector = icon,
        iconTint = MaterialTheme.colors.error
    )
}