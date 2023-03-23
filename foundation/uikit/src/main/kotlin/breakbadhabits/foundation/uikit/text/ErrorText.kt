package breakbadhabits.foundation.uikit.text

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import breakbadhabits.foundation.uikit.theme.AppTheme

@Composable
fun ErrorText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        color = AppTheme.colorScheme.error,
        text = text,
        imageVector = Icons.Default.Error,
        iconTint = AppTheme.colorScheme.error
    )
}