package epicarchitect.breakbadhabits.foundation.uikit.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button as MaterialButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text as MaterialText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.foundation.uikit.theme.AppTheme

object Button {
    enum class Type {
        Regular,
        Dangerous,
        Main;

        companion object {
            val Default = Regular
        }
    }
}

@Composable
fun Button(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    type: Button.Type = Button.Type.Default,
    icon: (@Composable () -> Unit)? = null
) {
    MaterialButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = CircleShape,
        elevation = when (type) {
            Button.Type.Main -> ButtonDefaults.buttonElevation(
                defaultElevation = 3.dp,
                pressedElevation = 1.dp,
                focusedElevation = 1.dp,
                hoveredElevation = 1.dp
            )

            else -> ButtonDefaults.buttonElevation(1.dp)
        },
        border = if (type == Button.Type.Dangerous) {
            BorderStroke(
                width = 0.5f.dp,
                color = AppTheme.colorScheme.primary
            )
        } else {
            null
        },
        colors = when (type) {
            Button.Type.Regular, Button.Type.Dangerous -> ButtonDefaults.buttonColors(
                containerColor = AppTheme.colorScheme.surface,
                contentColor = AppTheme.colorScheme.onSurface
            )

            Button.Type.Main -> ButtonDefaults.buttonColors(
                containerColor = AppTheme.colorScheme.primary,
                contentColor = AppTheme.colorScheme.onPrimary
            )
        },
        contentPadding = PaddingValues(
            top = 8.dp,
            bottom = 8.dp,
            start = if (icon != null) 16.dp else 24.dp,
            end = 24.dp
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                icon()
                Spacer(modifier = Modifier.padding(2.dp))
            }

            MaterialText(
                text = text,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
