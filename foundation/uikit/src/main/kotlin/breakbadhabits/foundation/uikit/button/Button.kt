package breakbadhabits.foundation.uikit.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import breakbadhabits.foundation.uikit.theme.AppTheme
import androidx.compose.material3.Button as MaterialButton
import androidx.compose.material3.Text as MaterialText

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
    var dangerousApproving by remember { mutableStateOf(false) }
    MaterialButton(
        onClick = {
            if (type == Button.Type.Dangerous) {
                dangerousApproving = dangerousApproving.not()
            } else {
                onClick()
            }
        },
        modifier = modifier,
        enabled = enabled,
        shape = MaterialTheme.shapes.small,
        elevation = when (type) {
            Button.Type.Main -> ButtonDefaults.buttonElevation(
                defaultElevation = 3.dp,
                pressedElevation = 1.dp,
                focusedElevation = 1.dp,
                hoveredElevation = 1.dp,
            )
            else -> ButtonDefaults.buttonElevation(1.dp)
        },
        border = if (type == Button.Type.Dangerous) {
            BorderStroke(
                width = 0.5f.dp,
                color = AppTheme.colorScheme.primary
            )
        } else null,
        colors = when (type) {
            Button.Type.Regular, Button.Type.Dangerous -> ButtonDefaults.buttonColors(
                containerColor = AppTheme.colorScheme.surface,
                contentColor = AppTheme.colorScheme.onSurface
            )

            Button.Type.Main -> ButtonDefaults.buttonColors(
                containerColor = AppTheme.colorScheme.primary,
                contentColor = AppTheme.colorScheme.onPrimary
            )
        }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                icon()
                Spacer(modifier = Modifier.padding(4.dp))
            }

            Box(
                contentAlignment = Alignment.Center
            ) {
                MaterialText(
                    text = text,
                    fontWeight = FontWeight.Medium,
                )

                androidx.compose.animation.AnimatedVisibility(visible = dangerousApproving) {
                    var value by remember { mutableStateOf(0f) }
                    val animatedValue by animateFloatAsState(targetValue = value)
                    Slider(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .matchParentSize(),
                        value = animatedValue,
                        onValueChange = { value = it },
                        onValueChangeFinished = {
                            if (value != 1f) {
                                value = 0f
                            } else {
                                onClick()
                            }
                        }
                    )
                }
            }

        }
    }
}