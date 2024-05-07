package epicarchitect.breakbadhabits.ui.dashboard

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
import androidx.compose.foundation.lazy.items
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
import epicarchitect.breakbadhabits.entity.datetime.duration
import epicarchitect.breakbadhabits.entity.icons.HabitIcons
import epicarchitect.breakbadhabits.entity.icons.VectorIcons
import epicarchitect.breakbadhabits.ui.appSettings.AppSettingsScreen
import epicarchitect.breakbadhabits.ui.habits.creation.HabitCreationScreen
import epicarchitect.breakbadhabits.ui.habits.details.FormattedDuration
import epicarchitect.breakbadhabits.ui.habits.details.HabitDetailsScreen
import epicarchitect.breakbadhabits.ui.habits.tracks.creation.HabitTrackCreationScreen
import epicarchitect.breakbadhabits.uikit.Card
import epicarchitect.breakbadhabits.uikit.FlowStateContainer
import epicarchitect.breakbadhabits.uikit.Icon
import epicarchitect.breakbadhabits.uikit.IconButton
import epicarchitect.breakbadhabits.uikit.button.Button
import epicarchitect.breakbadhabits.uikit.stateOfList
import epicarchitect.breakbadhabits.uikit.stateOfOneOrNull
import epicarchitect.breakbadhabits.uikit.text.Text

class DashboardScreen : Screen {
    @Composable
    override fun Content() {
        Dashboard()
    }
}

@Composable
fun Dashboard() {
    val navigator = LocalNavigator.currentOrThrow
    val resources = LocalDashboardResources.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = resources.titleText(),
                    type = Text.Type.Title,
                    priority = Text.Priority.High
                )

                IconButton(
                    onClick = {
                        navigator += AppSettingsScreen()
                    },
                    icon = VectorIcons.Settings
                )
            }

            FlowStateContainer(
                state = stateOfList { AppData.database.habitQueries.habits() }
            ) { items ->
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (items.isEmpty()) {
                        EmptyHabits()
                    } else {
                        LoadedHabits(items)
                    }

                    Button(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.BottomEnd),
                        onClick = {
                            navigator += HabitCreationScreen()
                        },
                        text = resources.newHabitButtonText(),
                        type = Button.Type.Main,
                        icon = {
                            Icon(VectorIcons.Add)
                        }
                    )
                }
            }
        }
    }
}


@Composable
private fun LoadedHabits(items: List<Habit>) {
    LazyColumn(
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 4.dp,
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

@Composable
private fun EmptyHabits() {
    val resources = LocalDashboardResources.current
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center,
            text = resources.emptyHabitsText()
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LazyItemScope.HabitCard(habit: Habit) {
    val navigator = LocalNavigator.currentOrThrow
    val resources = LocalDashboardResources.current
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
                    Icon(HabitIcons[habit.iconId])

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
                    Icon(VectorIcons.Time)

                    FlowStateContainer(
                        state = stateOfOneOrNull {
                            AppData.database.habitTrackQueries.trackByHabitIdAndMaxEndTime(habit.id)
                        }
                    ) { track ->
                        val appTime by AppData.userDateTime.collectAsState()
                        val abstinence = track?.let { (it.endTime..appTime.instant()).duration() }

                        Text(
                            modifier = Modifier.padding(start = 12.dp),
                            text = abstinence?.let {
                                FormattedDuration(
                                    value = it,
                                    accuracy = FormattedDuration.Accuracy.SECONDS
                                ).toString()
                            } ?: resources.habitHasNoEvents(),
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
                    navigator += HabitTrackCreationScreen(habit.id)
                },
                icon = VectorIcons.Replay
            )
        }
    }
}