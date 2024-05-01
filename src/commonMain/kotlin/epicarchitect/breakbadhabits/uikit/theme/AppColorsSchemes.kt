package epicarchitect.breakbadhabits.uikit.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object AppColorsSchemes {
    val dark = AppColorScheme(
        isDark = true,
        primary = Color(0xFFAF4448),
        onPrimary = Color(0xFFF1F1F1),
        secondary = Color(0xFFAF4448),
        onSecondary = Color(0xFFF1F1F1),
        onError = Color(0xFFF1F1F1),
        error = Color(0xFFE48801),
        background = Color.Black,
        onBackground = Color(0xFFF1F1F1),
        surface = Color(0xFF1C1B1F),
        onSurface = Color(0xFFCACACA)
    )

    val light = AppColorScheme(
        isDark = false,
        primary = Color(0xFFDF5353),
        secondary = Color(0xFFDF5353),
        onSecondary = Color(0xFF2C2C2C),
        onPrimary = Color(0xFFFFFFFF),
        onError = Color(0xFFF1F1F1),
        error = Color(0xFFff9800),
        background = Color.White,
        onBackground = Color(0xFF2C2C2C),
        surface = Color.White,
        onSurface = Color(0xFF2C2C2C)
    )

    val system: AppColorScheme
        @Composable
        get() = if (isSystemInDarkTheme()) dark else light
}