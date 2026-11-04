package epicarchitect.breakbadhabits.screens.habits.eventRecords.editing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.infrastructure.AppDatabase
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.cancel
import epicarchitect.breakbadhabits.resources.event_record_delete_confirmation
import epicarchitect.breakbadhabits.resources.yes
import epicarchitect.breakbadhabits.uikit.Dialog
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.Uuid

@Composable
fun HabitEventRecordDeletionDialog(
    recordId: Uuid,
    onDismiss: () -> Unit,
    onDeleted: () -> Unit
) {
    val habitEventRecordsDao = AppDatabase.habitEventRecordsDao
    val coroutineScope = rememberCoroutineScope()

    Dialog(onDismiss) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(Res.string.event_record_delete_confirmation),
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.align(Alignment.End)
            ) {
                Button(
                    onClick = onDismiss
                ) {
                    Text(
                        text = stringResource(Res.string.cancel)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            habitEventRecordsDao.deleteById(recordId)
                            onDeleted()
                        }
                    }
                ) {
                    Text(
                        text = stringResource(Res.string.yes)
                    )
                }
            }
        }
    }
}