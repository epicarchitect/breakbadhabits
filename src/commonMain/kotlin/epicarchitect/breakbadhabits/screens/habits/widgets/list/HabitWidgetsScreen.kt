package epicarchitect.breakbadhabits.screens.habits.widgets.list

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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.database.AppData
import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.database.HabitWidget
import epicarchitect.breakbadhabits.icons.VectorIcons
import epicarchitect.breakbadhabits.screens.habits.widgets.editing.HabitWidgetEditingScreen
import epicarchitect.breakbadhabits.uikit.Card
import epicarchitect.breakbadhabits.uikit.Icon
import epicarchitect.breakbadhabits.uikit.IconButton
import epicarchitect.breakbadhabits.uikit.text.Text
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

class HabitWidgetsScreen : Screen {
    @Composable
    override fun Content() {
        HabitAppWidgets()
    }
}

@Composable
fun HabitAppWidgets() {
    val navigator = LocalNavigator.currentOrThrow
    val resources = LocalHabitWidgetsResources.current

    val widgets by remember {
        AppData.mainDatabase.habitWidgetQueries
            .selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
    }.collectAsState(emptyList())

    val habits by remember {
        AppData.mainDatabase.habitQueries
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
            IconButton(onClick = navigator::pop) {
                Icon(VectorIcons.ArrowBack)
            }

            Spacer(Modifier.width(8.dp))

            Text(
                text = resources.title(),
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
                    text = resources.emptyList()
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
private fun WidgetConfigItem(item: HabitWidget, habits: List<Habit>) {
    val navigator = LocalNavigator.currentOrThrow

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    navigator += HabitWidgetEditingScreen(item.id)
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