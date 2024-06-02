package epicarchitect.breakbadhabits.ui.component

import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import epicarchitect.breakbadhabits.data.resources.icons.Icon
import androidx.compose.material3.Icon as MaterialIcon

@Immutable
data class VectorIcon(
    override val id: Int,
    val vector: ImageVector
) : Icon

@Composable
fun LocalResourceIcon(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
    MaterialIcon(
        modifier = modifier,
        imageVector = imageVector,
        contentDescription = null,
        tint = tint
    )
}

@Composable
fun LocalResourceIcon(
    resourceId: Int,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
//    MaterialIcon(
//        modifier = modifier,
//        painter = painterResource(resourceId),
//        contentDescription = null,
//        tint = tint,
//    )
}

@Composable
fun Icon(
    icon: Icon,
    modifier: Modifier = Modifier
) {
    if (icon is VectorIcon) {
        MaterialIcon(
            modifier = modifier,
            imageVector = icon.vector,
            contentDescription = null
        )
    }
}