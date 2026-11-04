package epicarchitect.breakbadhabits.uikit

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val AppDarkColorScheme = darkColorScheme(
    primary = Color(0xFFC05A5F),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF3A2024),
    onPrimaryContainer = Color(0xFFF7DADC),
    secondary = Color(0xFFE2A9AC),
    onSecondary = Color(0xFF341013),
    secondaryContainer = Color(0xFF42272A),
    onSecondaryContainer = Color(0xFFF7DADC),
    tertiary = Color(0xFFD99A9D),
    onTertiary = Color(0xFF321012),
    tertiaryContainer = Color(0xFF41272A),
    onTertiaryContainer = Color(0xFFF7DADC),
    background = Color(0xFF0C0A0D),
    onBackground = Color(0xFFF7F1F3),
    surface = Color(0xFF1B161C),
    onSurface = Color(0xFFF7F1F3),
    surfaceVariant = Color(0xFF2A222A),
    onSurfaceVariant = Color(0xFFBCADB4),
    surfaceTint = Color(0xFFC05A5F),
    surfaceDim = Color(0xFF0C0A0D),
    surfaceBright = Color(0xFF2C232B),
    surfaceContainerLowest = Color(0xFF0F0C10),
    surfaceContainerLow = Color(0xFF1B161C),
    surfaceContainer = Color(0xFF211A21),
    surfaceContainerHigh = Color(0xFF261E26),
    surfaceContainerHighest = Color(0xFF1B161C),
    outline = Color(0xFF5A4F57),
    outlineVariant = Color(0xFF3B3139),
    error = Color(0xFFE48A8E),
    onError = Color(0xFF2A1012),
    errorContainer = Color(0xFF5A2427),
    onErrorContainer = Color(0xFFFFDADB),
    inverseSurface = Color(0xFFF7F1F3),
    inverseOnSurface = Color(0xFF1B161C),
    inversePrimary = Color(0xFF8C3F43),
    scrim = Color.Black
)

private val AppTypography = Typography(
    displaySmall = TextStyle(
        fontSize = 32.sp,
        lineHeight = 36.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = (-0.8).sp
    ),
    headlineSmall = TextStyle(
        fontSize = 26.sp,
        lineHeight = 30.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = (-0.6).sp
    ),
    titleLarge = TextStyle(
        fontSize = 20.sp,
        lineHeight = 26.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = (-0.3).sp
    ),
    titleMedium = TextStyle(
        fontSize = 17.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = (-0.2).sp
    ),
    titleSmall = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle(
        fontSize = 15.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp
    ),
    bodyMedium = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp
    ),
    bodySmall = TextStyle(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp
    ),
    labelLarge = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.sp
    ),
    labelMedium = TextStyle(
        fontSize = 11.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.6.sp
    ),
    labelSmall = TextStyle(
        fontSize = 10.sp,
        lineHeight = 14.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.2.sp
    )
)

private val AppMaterialShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppDarkColorScheme,
        typography = AppTypography,
        shapes = AppMaterialShapes
    ) {
        CompositionLocalProvider(
            LocalContentColor provides MaterialTheme.colorScheme.onBackground,
            content = content
        )
    }
}

@Composable
fun Modifier.appGlow(): Modifier {
    val background = MaterialTheme.colorScheme.background
    val glow = MaterialTheme.colorScheme.primary
    return drawBehind {
        drawRect(background)
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    glow.copy(alpha = 0.20f),
                    Color.Transparent
                ),
                center = Offset(size.width / 2f, -size.height * 0.08f),
                radius = size.width * 1.20f
            )
        )
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.Transparent,
                    background
                ),
                startY = size.height * 0.24f,
                endY = size.height
            )
        )
    }
}
