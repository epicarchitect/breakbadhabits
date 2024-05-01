package epicarchitect.breakbadhabits.uikit

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.Image as FoundationImage

@Composable
fun Image(
    painter: Painter,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    colorFilter: ColorFilter? = null
) {
    FoundationImage(
        painter = painter,
        modifier = modifier,
        contentDescription = null,
        alignment = alignment,
        colorFilter = colorFilter
    )
}

@Composable
fun Image(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    colorFilter: ColorFilter? = null
) {
    FoundationImage(
        imageVector = imageVector,
        modifier = modifier,
        contentDescription = null,
        alignment = alignment,
        colorFilter = colorFilter
    )
}