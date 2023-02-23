package breakbadhabits.foundation.uikit.text

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ErrorText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        color = MaterialTheme.colorScheme.error,
        text = text,
        imageVector = Icons.Default.Error,
        iconTint = MaterialTheme.colorScheme.error
    )
}