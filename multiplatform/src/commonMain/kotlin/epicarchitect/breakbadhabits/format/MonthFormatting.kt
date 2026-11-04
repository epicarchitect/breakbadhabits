package epicarchitect.breakbadhabits.format

import androidx.compose.runtime.Composable
import epicarchitect.breakbadhabits.datetime.MonthOfYear
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.month_names
import org.jetbrains.compose.resources.stringArrayResource
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.uikit.PreviewTheme
import kotlinx.datetime.Month

@Composable
fun MonthOfYear.formatted(): String {
    val monthNames = stringArrayResource(Res.array.month_names)

    return "${monthNames[month.ordinal]} $year"
}

@Preview(widthDp = 260, heightDp = 100, name = "MonthOfYear")
@Composable
private fun MonthOfYearPreview() {
    PreviewTheme {
        Text(
            modifier = Modifier.padding(16.dp),
            text = MonthOfYear(2026, Month.NOVEMBER).formatted()
        )
    }
}
