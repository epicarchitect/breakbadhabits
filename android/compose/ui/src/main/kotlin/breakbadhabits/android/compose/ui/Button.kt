package breakbadhabits.android.compose.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.material.Button as MaterialButton

@Composable
fun Button(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small,
    enabled: Boolean = true,
    interactionType: InteractionType = InteractionType.REGULAR,
    elevation: Dp = 3.dp
) {
    MaterialButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        elevation = ButtonDefaults.elevation(elevation),
        border = if (interactionType == InteractionType.DANGEROUS) {
            BorderStroke(
                width = 0.5f.dp,
                color = MaterialTheme.colors.primary
            )
        } else null,
        colors = when (interactionType) {
            InteractionType.REGULAR, InteractionType.DANGEROUS -> ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = MaterialTheme.colors.onSurface
            )
            InteractionType.MAIN -> ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary
            )
        }
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Medium,
        )
    }
}