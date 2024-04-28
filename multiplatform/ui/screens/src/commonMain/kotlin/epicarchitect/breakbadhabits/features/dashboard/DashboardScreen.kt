package epicarchitect.breakbadhabits.features.dashboard

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
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import epicarchitect.breakbadhabits.UpdatingAppTime
import epicarchitect.breakbadhabits.features.LocalAppModule
import epicarchitect.breakbadhabits.foundation.coroutines.DefaultCoroutineDispatchers
import epicarchitect.breakbadhabits.foundation.datetime.duration
import epicarchitect.breakbadhabits.foundation.icons.get
import epicarchitect.breakbadhabits.foundation.uikit.Card
import epicarchitect.breakbadhabits.foundation.uikit.Icon
import epicarchitect.breakbadhabits.foundation.uikit.IconButton
import epicarchitect.breakbadhabits.foundation.uikit.button.Button
import epicarchitect.breakbadhabits.foundation.uikit.text.Text
import epicarchitect.breakbadhabits.sqldelight.main.Habit
import epicarchitect.breakbadhabits.ui.icons.VectorIcons
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import kotlin.time.Duration

@Composable
fun Dashboard(dependencies: DashboardDependencies) {
    val resources = dependencies.resources
    val navigation = dependencies.navigation

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
                    onClick = navigation::openAppSettings,
                    icon = VectorIcons.Settings
                )
            }

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                val items by dependencies.mainDatabase.habitQueries.selectAll()
                    .asFlow()
                    .mapToList(DefaultCoroutineDispatchers.io)
                    .map { it.sortedByDescending(Habit::id) }
                    .collectAsState(emptyList())

                if (items.isEmpty()) {
                    EmptyHabits(dependencies)
                } else {
                    LoadedHabits(items, dependencies)
                }

                Button(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomEnd),
                    onClick = navigation::openHabitCreation,
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
private fun LoadedHabits(
    items: List<Habit>,
    dependencies: DashboardDependencies
) {
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
            HabitCard(item, dependencies)
        }
    }
}

@Composable
private fun EmptyHabits(dependencies: DashboardDependencies) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center,
            text = dependencies.resources.emptyHabitsText()
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LazyItemScope.HabitCard(
    habit: Habit,
    dependencies: DashboardDependencies
) {
    val resources = dependencies.resources
    val navigation = dependencies.navigation
    val appModule = LocalAppModule.current
    val durationFormatter = appModule.format.durationFormatter

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateItemPlacement()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { navigation.openHabitDetails(habit.id) })
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
                    Icon(dependencies.habitIcons[habit.iconId])

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

                    val lastTrack by dependencies.mainDatabase.habitTrackQueries
                        .selectByHabitIdAndMaxEndTime(habit.id)
                        .asFlow()
                        .mapToOneOrNull(DefaultCoroutineDispatchers.io)
                        .collectAsState(null)

                    val appTime by UpdatingAppTime.state().collectAsState()
                    val abstinence = lastTrack?.let { LastHabitTrackAbstinence(it, appTime.instant()) }

                    Text(
                        modifier = Modifier.padding(start = 12.dp),
                        text = abstinence?.let {
                            durationFormatter.format(it.duration)
                        } ?: resources.habitHasNoEvents(),
                        type = Text.Type.Description,
                        priority = Text.Priority.Medium
                    )
                }
            }

            IconButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp),
                onClick = { navigation.openHabitTrackCreation(habit.id) },
                icon = VectorIcons.Replay
            )
        }
    }
}


interface HabitAbstinence : ClosedRange<Instant> {
    val duration: Duration
}

class RangeHabitAbstinence(
    range: ClosedRange<Instant>
) : HabitAbstinence, ClosedRange<Instant> by range {
    override val duration = range.duration()
}

class LastHabitTrackAbstinence(
    track: epicarchitect.breakbadhabits.sqldelight.main.HabitTrack,
    currentTime: Instant
) : HabitAbstinence by RangeHabitAbstinence(track.endTime..currentTime)