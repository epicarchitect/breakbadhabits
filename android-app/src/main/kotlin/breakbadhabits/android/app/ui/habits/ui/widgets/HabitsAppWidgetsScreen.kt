package breakbadhabits.android.app.ui.habits

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import breakbadhabits.android.app.R
import breakbadhabits.app.entity.HabitAppWidgetConfig
import breakbadhabits.foundation.controller.LoadingController
import breakbadhabits.foundation.uikit.Card
import breakbadhabits.foundation.uikit.LoadingBox
import breakbadhabits.foundation.uikit.text.Text

@Composable
fun HabitAppWidgetsScreen(
    widgetsLoadingController: LoadingController<List<HabitAppWidgetConfig>>,
    onWidgetClick: (HabitAppWidgetConfig.Id) -> Unit
) {
    LoadingBox(widgetsLoadingController) { widgets ->
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp),
                text = stringResource(R.string.main_widgets),
                type = Text.Type.Title,
                priority = Text.Priority.High
            )

            LazyColumn(
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 160.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(widgets, key = { it.id.value }) {
                    WidgetConfigItem(
                        config = it,
                        onClick = { onWidgetClick(it.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun WidgetConfigItem(
    config: HabitAppWidgetConfig,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    onClick()
                }
        ) {
            Text(
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 16.dp,
                    end = 54.dp
                ),
                text = config.title.value.ifEmpty { "#${config.id.value}" },
                type = Text.Type.Title
            )

            Text(
                modifier = Modifier.padding(
                    start = 16.dp,
                    bottom = 16.dp,
                    end = 16.dp
                ),
                text = buildString {
                    config.habitIds.forEachIndexed { index, habitId ->
                        append(habitId.value)
                        if (index != config.habitIds.lastIndex) {
                            appendLine()
                        }
                    }
                }
            )
        }
    }
}