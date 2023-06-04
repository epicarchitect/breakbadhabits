package epicarchitect.breakbadhabits.foundation.uikit.text

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.foundation.uikit.LocalResourceIcon
import epicarchitect.breakbadhabits.foundation.uikit.R
import epicarchitect.breakbadhabits.foundation.uikit.theme.AppTheme

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
            resourceId = R.drawable.uikit_error,
            tint = AppTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.width(8.dp))

        androidx.compose.material3.Text(
            text = text,
            color = AppTheme.colorScheme.error
        )
    }
}