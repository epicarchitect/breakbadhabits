package epicarchitect.breakbadhabits.format

import androidx.compose.runtime.Composable
import epicarchitect.breakbadhabits.datetime.MonthOfYear
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.month_names
import org.jetbrains.compose.resources.stringArrayResource

@Composable
fun MonthOfYear.formatted(): String {
    val monthNames = stringArrayResource(Res.array.month_names)

    return "${monthNames[month.ordinal]} $year"
}
