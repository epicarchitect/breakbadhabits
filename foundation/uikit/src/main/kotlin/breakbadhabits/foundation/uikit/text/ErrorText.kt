package breakbadhabits.foundation.uikit.text

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import breakbadhabits.foundation.uikit.LocalResourceIcon
import breakbadhabits.foundation.uikit.theme.AppTheme

@Composable
fun ErrorText(
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LocalResourceIcon(
            imageVector = Icons.Default.Error,
            tint = AppTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.width(8.dp))

        androidx.compose.material3.Text(
            text = text,
            color = AppTheme.colorScheme.error
        )
    }
}