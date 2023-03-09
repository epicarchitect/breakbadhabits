package breakbadhabits.foundation.uikit.text

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import breakbadhabits.foundation.uikit.Icon
import androidx.compose.material3.Text as MaterialText

@OptIn(ExperimentalAnimationApi::class)
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
    iconTint: Color = LocalContentColor.current,
    overflow: TextOverflow = TextOverflow.Clip
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (imageVector != null) {
            Icon(
                modifier = iconModifier,
                imageVector = imageVector,
                tint = iconTint
            )
        }

        AnimatedContent(
            targetState = text,
            transitionSpec = {
                fadeIn(tween(150)) with fadeOut(tween(250))
            }
        ) {
            MaterialText(
                text = it,
                style = style,
                color = color,
                fontWeight = fontWeight,
                fontSize = fontSize,
                textAlign = textAlign,
                overflow = overflow
            )
        }
    }
}