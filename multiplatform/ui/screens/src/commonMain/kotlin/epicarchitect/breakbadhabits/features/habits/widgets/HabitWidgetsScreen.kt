package epicarchitect.breakbadhabits.features.habits.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import epicarchitect.breakbadhabits.foundation.uikit.Card
import epicarchitect.breakbadhabits.foundation.uikit.Icon
import epicarchitect.breakbadhabits.foundation.uikit.IconButton
import epicarchitect.breakbadhabits.foundation.uikit.text.Text
import epicarchitect.breakbadhabits.sqldelight.main.Habit
import epicarchitect.breakbadhabits.sqldelight.main.HabitWidget
import epicarchitect.breakbadhabits.ui.icons.VectorIcons
import kotlinx.coroutines.Dispatchers

@Composable
fun HabitAppWidgets(dependencies: HabitWidgetsDependencies) {
    val widgets by remember(dependencies) {
        dependencies.mainDatabase.habitWidgetQueries
            .selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
    }.collectAsState(emptyList())

    val habits by remember(dependencies) {
        dependencies.mainDatabase.habitQueries
            .selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
    }.collectAsState(emptyList())

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = dependencies.navigation::back) {
                Icon(VectorIcons.ArrowBack)
            }

            Spacer(Modifier.width(8.dp))

            Text(
                text = dependencies.resources.title(),
                type = Text.Type.Title,
                priority = Text.Priority.High
            )
        }

        if (widgets.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center,
                    text = dependencies.resources.emptyList()
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
                items(widgets, key = { it.id }) { item ->
                    WidgetConfigItem(
                        item = item,
                        dependencies = dependencies,
                        habits = habits.filter {
                            item.habitIds.contains(it.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun WidgetConfigItem(
    item: HabitWidget,
    habits: List<Habit>,
    dependencies: HabitWidgetsDependencies,
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    dependencies.navigation.openWidget(item.id)
                }
        ) {
            Text(
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 16.dp,
                    end = 54.dp
                ),
                text = item.title.ifEmpty { "#${item.id}" },
                type = Text.Type.Title
            )

            Text(
                modifier = Modifier.padding(
                    start = 16.dp,
                    bottom = 16.dp,
                    end = 16.dp
                ),
                text = buildString {
                    habits.forEachIndexed { index, habit ->
                        append(habit.name)
                        if (index != habits.lastIndex) {
                            appendLine()
                        }
                    }
                }
            )
        }
    }
}