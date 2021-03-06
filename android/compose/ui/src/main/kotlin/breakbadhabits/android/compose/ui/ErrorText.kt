package breakbadhabits.android.compose.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import breakbadhabits.android.compose.ui.Text

@Composable
fun ErrorText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        color = MaterialTheme.colors.error,
        text = text,
        imageVector = Icons.Default.Error,
        iconTint = MaterialTheme.colors.error
    )
}