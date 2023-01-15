package breakbadhabits.ui.kit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button as MaterialButton

@Composable
fun Button(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small,
    enabled: Boolean = true,
    interactionType: InteractionType = InteractionType.REGULAR,
    elevation: Dp = 3.dp,
    icon: (@Composable () -> Unit)? = null
) {
    MaterialButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        elevation = ButtonDefaults.buttonElevation(elevation),
        border = if (interactionType == InteractionType.DANGEROUS) {
            BorderStroke(
                width = 0.5f.dp,
                color = MaterialTheme.colorScheme.primary
            )
        } else null,
        colors = when (interactionType) {
            InteractionType.REGULAR, InteractionType.DANGEROUS -> ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )

            InteractionType.MAIN -> ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                icon()
                Spacer(modifier = Modifier.padding(4.dp))
            }

            Text(
                text = text,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}