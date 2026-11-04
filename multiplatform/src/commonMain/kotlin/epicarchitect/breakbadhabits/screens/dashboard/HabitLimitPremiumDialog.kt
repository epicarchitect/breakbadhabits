package epicarchitect.breakbadhabits.screens.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.cancel
import epicarchitect.breakbadhabits.resources.dashboard_habit_limit_action
import epicarchitect.breakbadhabits.resources.dashboard_habit_limit_description
import epicarchitect.breakbadhabits.resources.dashboard_habit_limit_dialog_title
import epicarchitect.breakbadhabits.uikit.Dialog
import epicarchitect.breakbadhabits.uikit.PreviewTheme
import epicarchitect.breakbadhabits.uikit.PrimaryButton
import epicarchitect.breakbadhabits.uikit.SecondaryButton
import org.jetbrains.compose.resources.stringResource

@Composable
fun HabitLimitPremiumDialog(
    onDismiss: () -> Unit,
    onOpenPremium: () -> Unit
) {
    Dialog(onDismiss) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = stringResource(Res.string.dashboard_habit_limit_dialog_title),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(Res.string.dashboard_habit_limit_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(20.dp))

            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.dashboard_habit_limit_action),
                onClick = onOpenPremium
            )

            Spacer(Modifier.height(10.dp))

            SecondaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.cancel),
                onClick = onDismiss
            )
        }
    }
}

@Preview(widthDp = 360, heightDp = 420, name = "HabitLimitPremiumDialog")
@Composable
private fun HabitLimitPremiumDialogPreview() {
    PreviewTheme {
        HabitLimitPremiumDialog(
            onDismiss = {},
            onOpenPremium = {}
        )
    }
}
