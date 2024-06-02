package epicarchitect.breakbadhabits.ui.component.text

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.ui.component.theme.AppTheme

@Composable
fun ErrorText(
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Error,
            tint = AppTheme.colorScheme.error,
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = text,
            color = AppTheme.colorScheme.error
        )
    }
}