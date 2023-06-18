package epicarchitect.breakbadhabits.foundation.uikit.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.foundation.uikit.Dialog
import kotlinx.datetime.Instant

@Composable
fun SelectionEpicCalendarDialog(
    state: IntervalSelectionEpicCalendarState,
    onSelected: (ClosedRange<Instant>) -> Unit,
    onCancel: () -> Unit
) {
    Dialog(onDismiss = onCancel) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            IntervalSelectionEpicCalendar(
                state = state,
                onSelected = onSelected,
                intervalsInnerPadding = 8.dp,
                onCancel = onCancel
            )
        }
    }
}
