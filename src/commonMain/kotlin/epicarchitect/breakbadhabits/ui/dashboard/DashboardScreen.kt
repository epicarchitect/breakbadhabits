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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.database.AppData
import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.entity.datetime.duration
import epicarchitect.breakbadhabits.entity.icons.HabitIcons
import epicarchitect.breakbadhabits.entity.icons.VectorIcons
import epicarchitect.breakbadhabits.entity.time.UpdatingAppTime
import epicarchitect.breakbadhabits.ui.appSettings.AppSettingsScreen
import epicarchitect.breakbadhabits.ui.habits.creation.HabitCreationScreen
import epicarchitect.breakbadhabits.ui.habits.details.HabitDetailsScreen
import epicarchitect.breakbadhabits.ui.habits.tracks.creation.HabitTrackCreationScreen
import epicarchitect.breakbadhabits.uikit.Card
import epicarchitect.breakbadhabits.uikit.Icon
import epicarchitect.breakbadhabits.uikit.IconButton
import epicarchitect.breakbadhabits.uikit.button.Button
import epicarchitect.breakbadhabits.uikit.text.Text
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.map

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

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                val items by remember {
                    AppData.database.habitQueries.selectAll()
                        .asFlow()
                        .mapToList(Dispatchers.IO)
                        .map { it.sortedByDescending(Habit::id) }
                }.collectAsState(emptyList())

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

                    val lastTrack by AppData.database.habitTrackQueries
                        .selectByHabitIdAndMaxEndTime(habit.id)
                        .asFlow()
                        .mapToOneOrNull(Dispatchers.IO)
                        .collectAsState(null)

                    val appTime by UpdatingAppTime.state().collectAsState()
                    val abstinence = lastTrack?.let { (it.endTime..appTime.instant()).duration() }

                    Text(
                        modifier = Modifier.padding(start = 12.dp),
                        text = abstinence?.toString() ?: resources.habitHasNoEvents(),
                        type = Text.Type.Description,
                        priority = Text.Priority.Medium
                    )
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