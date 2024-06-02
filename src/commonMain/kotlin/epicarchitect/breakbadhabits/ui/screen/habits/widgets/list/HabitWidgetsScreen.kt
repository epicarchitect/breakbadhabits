package epicarchitect.breakbadhabits.ui.screen.habits.widgets.list

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.data.AppData
import epicarchitect.breakbadhabits.data.Habit
import epicarchitect.breakbadhabits.data.HabitWidget
import epicarchitect.breakbadhabits.ui.screen.habits.widgets.editing.HabitWidgetEditingScreen
import epicarchitect.breakbadhabits.ui.component.Card
import epicarchitect.breakbadhabits.ui.component.FlowStateContainer
import epicarchitect.breakbadhabits.ui.component.Icon
import epicarchitect.breakbadhabits.ui.component.IconButton
import epicarchitect.breakbadhabits.ui.component.stateOfList
import epicarchitect.breakbadhabits.ui.component.text.Text

class HabitWidgetsScreen : Screen {
    @Composable
    override fun Content() {
        HabitAppWidgets()
    }
}

@Composable
fun HabitAppWidgets() {
    val navigator = LocalNavigator.currentOrThrow
    val icons = AppData.resources.icons
    val habitWidgetsStrings = AppData.resources.strings.habitWidgetsStrings

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = navigator::pop) {
                Icon(icons.commonIcons.arrowLeft)
            }

            Spacer(Modifier.width(8.dp))

            Text(
                text = habitWidgetsStrings.title(),
                type = Text.Type.Title,
                priority = Text.Priority.High
            )
        }

        FlowStateContainer(
            state1 = stateOfList { AppData.database.habitWidgetQueries.widgets() },
            state2 = stateOfList { AppData.database.habitQueries.habits() }
        ) { widgets, habits ->
            if (widgets.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center,
                        text = habitWidgetsStrings.emptyList()
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