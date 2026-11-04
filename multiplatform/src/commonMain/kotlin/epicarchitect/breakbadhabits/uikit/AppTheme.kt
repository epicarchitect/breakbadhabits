package epicarchitect.breakbadhabits.uikit

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AppColorScheme = darkColorScheme(
    primary = Color(0xFFAF4448),
    onPrimary = Color(0xFFF1F1F1),
    secondary = Color(0xFFAF4448),
    onSecondary = Color(0xFFF1F1F1),
    tertiary = Color(0xFFAF4448),
    onTertiary = Color(0xFFF1F1F1),
    onError = Color(0xFFF1F1F1),
    error = Color(0xFFE48801),
    background = Color.Black,
    onBackground = Color(0xFFF1F1F1),
    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFCACACA),
    onPrimaryContainer = Color(0xFFF1F1F1),
    primaryContainer = Color(0xFFAF4448),
    surfaceContainerHighest = Color(0xFF1C1B1F)
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        content = content
    )
}
