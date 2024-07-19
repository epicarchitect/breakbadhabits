package epicarchitect.breakbadhabits.screens.habits.widgets.dashboard

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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.Environment
import epicarchitect.breakbadhabits.uikit.Card
import epicarchitect.breakbadhabits.uikit.FlowStateContainer
import epicarchitect.breakbadhabits.uikit.SimpleTopAppBar
import epicarchitect.breakbadhabits.uikit.animatedShadowElevation
import epicarchitect.breakbadhabits.uikit.stateOfList
import epicarchitect.breakbadhabits.uikit.text.Text
import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.database.HabitWidget
import epicarchitect.breakbadhabits.screens.habits.widgets.editing.HabitWidgetEditingScreen

class HabitWidgetsDashboardScreen : Screen {
    @Composable
    override fun Content() {
        HabitWidgetsDashboard()
    }
}

@Composable
fun HabitWidgetsDashboard() {
    val navigator = LocalNavigator.currentOrThrow
    val strings = Environment.resources.strings.habitWidgetsDashboardStrings

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val listState = rememberLazyListState()
        val shadowElevation by listState.animatedShadowElevation()
        SimpleTopAppBar(
            title = strings.title(),
            onBackClick = navigator::pop,
            shadowElevation = shadowElevation
        )

        FlowStateContainer(
            state1 = stateOfList { Environment.database.habitWidgetQueries.widgets() },
            state2 = stateOfList { Environment.database.habitQueries.habits() }
        ) { widgets, habits ->
            if (widgets.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center,
                        text = strings.emptyList()
                    )
                }
            } else {
                LazyColumn(
                    state = listState,
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
private fun WidgetConfigItem(
    item: HabitWidget,
    habits: List<Habit>
) {
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