package breakbadhabits.ui.kit

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import breakbadhabits.ui.kit.Icon
import androidx.compose.material.Text as MaterialText

@Composable
fun Text(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    style: TextStyle = LocalTextStyle.current,
    fontWeight: FontWeight? = null,
    iconModifier: Modifier = Modifier.padding(end = 8.dp),
    imageVector: ImageVector? = null,
    textAlign: TextAlign? = null,
    iconTint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (imageVector != null) {
            Icon(
                modifier = iconModifier,
                imageVector = imageVector,
                tint = iconTint
            )
        }

        MaterialText(
            text = text,
            style = style,
            color = color,
            fontWeight = fontWeight,
            fontSize = fontSize,
            textAlign = textAlign
        )
    }
}