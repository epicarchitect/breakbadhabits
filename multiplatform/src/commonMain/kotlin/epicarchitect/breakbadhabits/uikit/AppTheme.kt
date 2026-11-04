package epicarchitect.breakbadhabits.uikit

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AppDarkColorScheme = darkColorScheme(
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
    surfaceContainerLow = Color(0xFF211F24),
    surfaceContainerHighest = Color(0xFF1C1B1F),
    outlineVariant = Color(0xFF49454F)
)

private val AppLightColorScheme = lightColorScheme(
    primary = Color(0xFFAF4448),
    onPrimary = Color.White,
    secondary = Color(0xFFAF4448),
    onSecondary = Color.White,
    tertiary = Color(0xFFAF4448),
    onTertiary = Color.White,
    error = Color(0xFFB3261E),
    onError = Color.White,
    background = Color(0xFFF8F6F3),
    onBackground = Color(0xFF201A1B),
    surface = Color(0xFFFFFBFF),
    onSurface = Color(0xFF49454F),
    primaryContainer = Color(0xFFF4DADB),
    onPrimaryContainer = Color(0xFF3B090B),
    surfaceContainerLow = Color(0xFFF2EFED),
    surfaceContainerHighest = Color(0xFFE8E3E1),
    outlineVariant = Color(0xFFD4C4C4)
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    InstantProvider {
        MaterialTheme(
            colorScheme = AppDarkColorScheme,
            content = content
        )
    }
}
