package breakbadhabits.framework.uikit

import android.annotation.SuppressLint
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@SuppressLint("ConflictingOnColor")
private val lightColors = lightColorScheme(
//    primary = Color(0xFFDF5353),
//    secondary = Color(0xFFDF5353),
//    onSecondary = Color(0xFF2C2C2C),
//    onPrimary = Color(0xFFFFFFFF),
//    onError = Color(0xFFF1F1F1),
//    error = Color(0xFFff9800),
//    background = Color.White
)

@SuppressLint("ConflictingOnColor")
private val darkColors = darkColorScheme(
//    primary = Color(0xFFAF4448),
//    secondary = Color(0xFFAF4448),
//    onSecondary = Color(0xFFF1F1F1),
//    onPrimary = Color(0xFFF1F1F1),
//    onError = Color(0xFFF1F1F1),
//    error = Color(0xFFE48801),
//    background = Color.Black
)

@Composable
fun Theme(
    isDarkTheme: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (isDarkTheme) darkColors else lightColors,
        shapes = Shapes(
            small = RoundedCornerShape(12.dp),
            medium = RoundedCornerShape(24.dp),
            large = RoundedCornerShape(24.dp),
        )
    ) {
        Surface(
            color = MaterialTheme.colorScheme.background,
            content = content
        )
    }
}