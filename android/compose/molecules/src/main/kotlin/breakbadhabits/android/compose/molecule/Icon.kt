package breakbadhabits.android.compose.molecule

import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.Icon as MaterialIcon

@Composable
fun Icon(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
) {
    MaterialIcon(
        modifier = modifier,
        imageVector = imageVector,
        contentDescription = null,
        tint = tint,
    )
}

@Composable
fun Icon(
    painter: Painter,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
) {
    MaterialIcon(
        modifier = modifier,
        painter = painter,
        contentDescription = null,
        tint = tint,
    )
}