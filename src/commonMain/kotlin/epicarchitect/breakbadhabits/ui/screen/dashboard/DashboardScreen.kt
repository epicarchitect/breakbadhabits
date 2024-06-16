package epicarchitect.breakbadhabits.ui.screen.dashboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.data.AppData
import epicarchitect.breakbadhabits.data.Habit
import epicarchitect.breakbadhabits.operation.habits.abstinence
import epicarchitect.breakbadhabits.ui.component.Card
import epicarchitect.breakbadhabits.ui.component.FlowStateContainer
import epicarchitect.breakbadhabits.ui.component.Icon
import epicarchitect.breakbadhabits.ui.component.IconButton
import epicarchitect.breakbadhabits.ui.component.SimpleTopAppBar
import epicarchitect.breakbadhabits.ui.component.animatedShadowElevation
import epicarchitect.breakbadhabits.ui.component.button.Button
import epicarchitect.breakbadhabits.ui.component.stateOfList
import epicarchitect.breakbadhabits.ui.component.stateOfOneOrNull
import epicarchitect.breakbadhabits.ui.component.text.Text
import epicarchitect.breakbadhabits.ui.format.DurationFormattingAccuracy
import epicarchitect.breakbadhabits.ui.format.formatted
import epicarchitect.breakbadhabits.ui.screen.appSettings.AppSettingsScreen
import epicarchitect.breakbadhabits.ui.screen.habits.creation.HabitCreationScreen
import epicarchitect.breakbadhabits.ui.screen.habits.details.HabitDetailsScreen
import epicarchitect.breakbadhabits.ui.screen.habits.records.creation.HabitEventRecordCreationScreen

class DashboardScreen : Screen {
    @Composable
    override fun Content() {
        Dashboard()
    }
}

@Composable
fun Dashboard() {
    val navigator = LocalNavigator.currentOrThrow
    val strings = AppData.resources.strings.dashboardStrings
    val icons = AppData.resources.icons
    val habitQueries = AppData.database.habitQueries

    FlowStateContainer(
        state = stateOfList { habitQueries.habits() }
    ) { items ->
        Column {
            val listState = rememberLazyListState()
            val shadowElevation by listState.animatedShadowElevation()

            SimpleTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = strings.titleText(),
                shadowElevation = shadowElevation,
                actions = {
                    IconButton(
                        onClick = { navigator += AppSettingsScreen() },
                        icon = icons.commonIcons.settings
                    )
                }
            )
            Content(listState, items)
        }
    }
}

@Composable
private fun Content(
    listState: LazyListState,
    items: List<Habit>
) {
    val navigator = LocalNavigator.currentOrThrow
    val strings = AppData.resources.strings.dashboardStrings
    val icons = AppData.resources.icons

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        if (items.isEmpty()) {
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Center),
                textAlign = TextAlign.Center,
                text = strings.emptyHabitsText()
            )
        } else {
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 100.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = items,
                    key = { it.id }
                ) { item ->
                    HabitCard(item)
                }
            }
        }

        Button(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = { navigator += HabitCreationScreen() },
            text = strings.newHabitButtonText(),
            icon = { Icon(icons.commonIcons.add) },
            type = Button.Type.Main
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LazyItemScope.HabitCard(habit: Habit) {
    val navigator = LocalNavigator.currentOrThrow
    val strings = AppData.resources.strings.dashboardStrings
    val icons = AppData.resources.icons

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateItemPlacement()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navigator += HabitDetailsScreen(habit.id)
                }
        ) {
            Column(
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 16.dp,
                    bottom = 16.dp,
                    end = 50.dp
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(icons.habitIcons.getById(habit.iconId))

                    Text(
                        modifier = Modifier.padding(start = 12.dp),
                        text = habit.name,
                        type = Text.Type.Title,
                        priority = Text.Priority.Medium
                    )
                }
                Row(
                    modifier = Modifier.padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(icons.commonIcons.time)

                    FlowStateContainer(
                        state = stateOfOneOrNull {
                            AppData.database.habitEventRecordQueries.recordByHabitIdAndMaxEndTime(habit.id)
                        }
                    ) { record ->
                        val currentTime by AppData.dateTime.currentTimeState.collectAsState()
                        val abstinence = record?.abstinence(currentTime)

                        Text(
                            modifier = Modifier.padding(start = 12.dp),
                            text = abstinence?.formatted(
                                accuracy = DurationFormattingAccuracy.SECONDS
                            ) ?: strings.habitHasNoEvents(),
                            type = Text.Type.Description,
                            priority = Text.Priority.Medium
                        )
                    }
                }
            }

            IconButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp),
                onClick = {
                    navigator += HabitEventRecordCreationScreen(habit.id)
                },
                icon = icons.commonIcons.replay
            )
        }
    }
}