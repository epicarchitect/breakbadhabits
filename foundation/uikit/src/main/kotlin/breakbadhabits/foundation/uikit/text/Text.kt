package breakbadhabits.foundation.uikit.text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text as MaterialText

object Text {
    enum class Type {
        Headline,
        Title,
        Label,
        Regular;

        companion object {
            val Default = Regular
        }
    }
}

@Composable
fun Text(
    text: String,
    modifier: Modifier = Modifier,
    type: Text.Type = Text.Type.Default,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
) {
    MaterialText(
        modifier = modifier,
        text = text,
        style = when (type) {
            Text.Type.Headline -> TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 22.sp
            )
            Text.Type.Title -> TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 18.sp
            )
            Text.Type.Label -> TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 12.sp
            )
            Text.Type.Regular -> TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 14.sp
            )
        },
        textAlign = textAlign,
        overflow = overflow
    )
}