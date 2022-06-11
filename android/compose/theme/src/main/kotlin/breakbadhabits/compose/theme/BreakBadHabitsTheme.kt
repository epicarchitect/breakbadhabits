package breakbadhabits.compose.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@SuppressLint("ConflictingOnColor")
val lightColors = lightColors(
    primary = Color(0xFFF44336),
    primaryVariant = Color(0xFFF44336),
    secondary = Color(0xFFF44336),
    onSecondary = Color(0xFF2C2C2C),
    onPrimary = Color(0xFFF1F1F1),
    onError = Color(0xFFF1F1F1),
    error = Color(0xFFff9800),
    background = Color.White
)

@SuppressLint("ConflictingOnColor")
val darkColors = darkColors(
    primary = Color(0xFFDD3E32),
    primaryVariant = Color(0xFFDD3E32),
    secondary = Color(0xFFDD3E32),
    onSecondary = Color(0xFFF1F1F1),
    onPrimary = Color(0xFFF1F1F1),
    onError = Color(0xFFF1F1F1),
    error = Color(0xFFE48801),
    background = Color.Black
)

@Composable
fun BreakBadHabitsTheme(
    isDarkTheme: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (isDarkTheme) darkColors else lightColors,
        shapes = Shapes(
            small = RoundedCornerShape(12.dp),
            medium = RoundedCornerShape(24.dp),
            large = RoundedCornerShape(24.dp),
        ),
    ) {
        Surface(
            color = MaterialTheme.colors.background,
            content = content
        )
    }
}
