package epicarchitect.breakbadhabits.ui.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.environment.resources.icons.Icon
import epicarchitect.breakbadhabits.ui.component.Icon
import epicarchitect.breakbadhabits.ui.component.theme.AppTheme
import androidx.compose.material3.Button as MaterialButton
import androidx.compose.material3.Text as MaterialText

@Immutable
data class ButtonStyle(
    val elevation: Dp,
    val shape: Shape,
    val border: BorderStroke?,
    val containerColor: Color,
    val contentColor: Color
)

object ButtonStyles {
    val regular
        @Composable
        get() = ButtonStyle(
            elevation = 1.dp,
            shape = CircleShape,
            border = null,
            contentColor = AppTheme.colorScheme.onSurface,
            containerColor = AppTheme.colorScheme.surface,
        )

    val primary
        @Composable
        get() = ButtonStyle(
            elevation = 1.dp,
            shape = CircleShape,
            border = null,
            containerColor = AppTheme.colorScheme.primary,
            contentColor = AppTheme.colorScheme.onPrimary
        )

    val dangerous
        @Composable
        get() = ButtonStyle(
            elevation = 1.dp,
            shape = CircleShape,
            border = BorderStroke(
                width = 0.5f.dp,
                color = AppTheme.colorScheme.primary
            ),
            contentColor = AppTheme.colorScheme.onSurface,
            containerColor = AppTheme.colorScheme.surface,
        )
}

@Composable
fun Button(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: ButtonStyle = ButtonStyles.regular,
    icon: Icon? = null
) {
    MaterialButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = CircleShape,
        elevation = ButtonDefaults.buttonElevation(style.elevation),
        border = style.border,
        colors = ButtonDefaults.buttonColors(
            containerColor = style.containerColor,
            contentColor = style.contentColor
        ),
        contentPadding = PaddingValues(
            top = 8.dp,
            bottom = 8.dp,
            start = if (icon != null) 16.dp else 24.dp,
            end = 24.dp
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(icon)
                Spacer(modifier = Modifier.padding(2.dp))
            }

            MaterialText(
                text = text,
                fontWeight = FontWeight.Medium
            )
        }
    }
}