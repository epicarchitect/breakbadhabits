package epicarchitect.breakbadhabits.screens.habits.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.foundation.uikit.Card
import epicarchitect.breakbadhabits.foundation.uikit.LoadingBox
import epicarchitect.breakbadhabits.foundation.uikit.text.Text
import epicarchitect.breakbadhabits.presentation.habits.HabitAppWidgetsViewModel

@Composable
fun HabitAppWidgets(
    viewModel: HabitAppWidgetsViewModel,
    onWidgetClick: (widgetId: Int) -> Unit
) {
    LoadingBox(viewModel.itemsController) { widgets ->
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp),
                text = "stringResource(R.string.main_widgets)",
                type = Text.Type.Title,
                priority = Text.Priority.High
            )

            if (widgets.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center,
                        text = "stringResource(R.string.habitsAppWidgets_empty)"
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = 160.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(widgets, key = { it.widget.id }) {
                        WidgetConfigItem(
                            item = it,
                            onClick = { onWidgetClick(it.widget.id) }
                        )
                    }
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
                text = item.widget.title.ifEmpty { "#${item.widget.id}" },
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
                        append(habit.name)
                        if (index != item.habits.lastIndex) {
                            appendLine()
                        }
                    }
                }
            )
        }
    }
}