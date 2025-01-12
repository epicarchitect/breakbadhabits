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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.environment.LocalAppEnvironment
import epicarchitect.breakbadhabits.uikit.Dialog

@Composable
fun HabitEventRecordDeletionDialog(
    recordId: Int,
    onDismiss: () -> Unit,
    onDeleted: () -> Unit
) {
    val environment = LocalAppEnvironment.current
    val strings = environment.resources.strings.habitEventRecordEditingStrings
    val habitEventRecordQueries = environment.database.habitEventRecordQueries

    Dialog(onDismiss) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = strings.deleteConfirmation(),
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
                        text = strings.cancel()
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        habitEventRecordQueries.deleteById(recordId)
                        onDeleted()
                    }
                ) {
                    Text(
                        text = strings.yes()
                    )
                }
            }
        }
    }
}