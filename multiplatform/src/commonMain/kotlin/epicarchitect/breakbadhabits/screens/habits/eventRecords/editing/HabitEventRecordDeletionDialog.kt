package epicarchitect.breakbadhabits.screens.habits.eventRecords.editing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import epicarchitect.breakbadhabits.infrastructure.AppDatabase
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.cancel
import epicarchitect.breakbadhabits.resources.event_record_delete_confirmation
import epicarchitect.breakbadhabits.resources.yes
import epicarchitect.breakbadhabits.uikit.AsyncAction
import epicarchitect.breakbadhabits.uikit.AsyncPrimaryButton
import epicarchitect.breakbadhabits.uikit.Dialog
import epicarchitect.breakbadhabits.uikit.PreviewTheme
import epicarchitect.breakbadhabits.uikit.SecondaryButton
import epicarchitect.breakbadhabits.uikit.rememberAsyncAction
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.Uuid

@Composable
fun HabitEventRecordDeletionDialog(
    recordId: Uuid,
    onDismiss: () -> Unit,
    onDeleted: () -> Unit
) {
    val deleteAction = rememberAsyncAction {
        AppDatabase.habitEventRecordsDao.deleteById(recordId)
        onDeleted()
    }

    Dialog(onDismiss = { if (!deleteAction.isRunning) onDismiss() }) {
        HabitEventRecordDeletionDialogContent(
            action = deleteAction,
            onDismiss = onDismiss
        )
    }
}

@Composable
private fun HabitEventRecordDeletionDialogContent(
    action: AsyncAction,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = stringResource(Res.string.event_record_delete_confirmation),
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SecondaryButton(
                modifier = Modifier.weight(1f),
                text = stringResource(Res.string.cancel),
                enabled = !action.isRunning,
                onClick = onDismiss
            )

            AsyncPrimaryButton(
                modifier = Modifier.weight(1f),
                action = action,
                text = stringResource(Res.string.yes)
            )
        }
    }
}

@Preview(widthDp = 360, heightDp = 220, name = "HabitEventRecordDeletionDialogContent")
@Composable
private fun HabitEventRecordDeletionDialogContentPreview() {
    PreviewTheme {
        Dialog {
            HabitEventRecordDeletionDialogContent(
                action = PreviewAsyncAction,
                onDismiss = {}
            )
        }
    }
}

private object PreviewAsyncAction : AsyncAction {
    override val isRunning = false
    override fun invoke() = Unit
}
