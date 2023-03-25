package breakbadhabits.android.app.ui.habits.ui.widgets

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
import breakbadhabits.app.presentation.habits.HabitAppWidgetsViewModel
import breakbadhabits.foundation.controller.LoadingController
import breakbadhabits.foundation.uikit.Card
import breakbadhabits.foundation.uikit.LoadingBox
import breakbadhabits.foundation.uikit.text.Text

@Composable
fun HabitAppWidgetsScreen(
    itemsController: LoadingController<List<HabitAppWidgetsViewModel.Item>>,
    onWidgetClick: (HabitAppWidgetConfig.Id) -> Unit
) {
    LoadingBox(itemsController) { widgets ->
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
                items(widgets, key = { it.config.id.value }) {
                    WidgetConfigItem(
                        item = it,
                        onClick = { onWidgetClick(it.config.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun WidgetConfigItem(
    item: HabitAppWidgetsViewModel.Item,
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
                text = item.config.title.value.ifEmpty { "#${item.config.id.value}" },
                type = Text.Type.Title
            )

            Text(
                modifier = Modifier.padding(
                    start = 16.dp,
                    bottom = 16.dp,
                    end = 16.dp
                ),
                text = buildString {
                    item.habits.forEachIndexed { index, habit ->
                        append(habit.name.value)
                        if (index != item.habits.lastIndex) {
                            appendLine()
                        }
                    }
                }
            )
        }
    }
}