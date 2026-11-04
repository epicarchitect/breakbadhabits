package epicarchitect.breakbadhabits.screens.habits.widgets.creation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import epicarchitect.breakbadhabits.uikit.AppTheme

@Composable
fun HabitWidgetCreationRoot(
    systemWidgetId: Int,
    onDone: () -> Unit
) {
    AppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding()
        ) {
            HabitWidgetCreationScreen(
                systemWidgetId = systemWidgetId,
                onDone = onDone
            )
        }
    }
}
