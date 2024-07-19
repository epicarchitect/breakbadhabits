package epicarchitect.breakbadhabits.uikit.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val LocalAppColorScheme = staticCompositionLocalOf<AppColorScheme> {
    error("LocalAppColorScheme not present")
}

private fun AppColorScheme.toMaterial() = if (isDark) {
    darkColorScheme(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primary.copy(alpha = 0.1f),
        onPrimaryContainer = onSurface,
        error = error,
        onError = onError,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface
    )
} else {
    lightColorScheme(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primary.copy(alpha = 0.1f),
        onPrimaryContainer = onSurface,
        error = error,
        onError = onError,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface
    )
}

object AppTheme {
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
                large = RoundedCornerShape(24.dp)
            ),
            typography = Typography(
                titleLarge = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                ),
                titleMedium = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                ),
                titleSmall = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                ),
                bodyLarge = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                ),
                bodyMedium = MaterialTheme.typography.bodyMedium,
                bodySmall = MaterialTheme.typography.bodySmall,
                labelLarge = MaterialTheme.typography.labelLarge,
                labelMedium = MaterialTheme.typography.labelMedium,
                labelSmall = MaterialTheme.typography.labelSmall
            )
        ) {
            Surface(
                color = AppTheme.colorScheme.background
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding()
                ) {
                    content()
                }
            }
        }
    }
}