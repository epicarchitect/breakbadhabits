package breakbadhabits.foundation.uikit.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

private val LocalAppColorScheme = staticCompositionLocalOf<AppColorScheme> {
    error("LocalAppColorScheme not present")
}

private fun AppColorScheme.toMaterial() = if (isDark) {
    darkColorScheme(
        primary = primary,
        onPrimary = onPrimary,
        error = error,
        onError = onError,
        background = background,
        onBackground = onBackground
    )
} else {
    lightColorScheme(
        primary = primary,
        onPrimary = onPrimary,
        error = error,
        onError = onError,
        background = background,
        onBackground = onBackground
    )
}

internal object AppTheme {
    val colorScheme: AppColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current
}


@Composable
fun AppTheme(
    colorScheme: AppColorScheme,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalAppColorScheme provides colorScheme
    ) {
        MaterialTheme(
            colorScheme = colorScheme.toMaterial(),
            shapes = Shapes(
                small = RoundedCornerShape(12.dp),
                medium = RoundedCornerShape(24.dp),
                large = RoundedCornerShape(24.dp),
            )
        ) {
            Surface(
                color = AppTheme.colorScheme.background,
                content = content
            )
        }
    }
}