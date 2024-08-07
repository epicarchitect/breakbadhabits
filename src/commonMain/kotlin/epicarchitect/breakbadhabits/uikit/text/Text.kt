package epicarchitect.breakbadhabits.uikit.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.material3.Text as MaterialText

object Text {
    enum class Type {
        Title,
        Description,
        Label;

        companion object {
            val Default = Description
        }
    }

    enum class Priority {
        High,
        Medium,
        Low;

        companion object {
            val Default = Medium
        }
    }
}

@Composable
fun Text(
    text: CharSequence,
    modifier: Modifier = Modifier,
    type: Text.Type = Text.Type.Default,
    priority: Text.Priority = Text.Priority.Default,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    color: Color = Color.Unspecified
) {
    MaterialText(
        modifier = modifier,
        color = color,
        text = text.toString(),
        style = when (type) {
            Text.Type.Title -> when (priority) {
                Text.Priority.High -> MaterialTheme.typography.titleLarge
                Text.Priority.Medium -> MaterialTheme.typography.titleMedium
                Text.Priority.Low -> MaterialTheme.typography.titleSmall
            }

            Text.Type.Label -> when (priority) {
                Text.Priority.High -> MaterialTheme.typography.labelLarge
                Text.Priority.Medium -> MaterialTheme.typography.labelMedium
                Text.Priority.Low -> MaterialTheme.typography.labelSmall
            }

            Text.Type.Description -> when (priority) {
                Text.Priority.High -> MaterialTheme.typography.bodyLarge
                Text.Priority.Medium -> MaterialTheme.typography.bodyMedium
                Text.Priority.Low -> MaterialTheme.typography.bodySmall
            }
        },
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines
    )
}