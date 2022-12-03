package breakbadhabits.android.app.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import breakbadhabits.android.app.LocalHabitAbstinenceFormatter
import breakbadhabits.android.app.LocalHabitIconResources
import breakbadhabits.android.app.LocalPresentationModule
import breakbadhabits.android.app.R
import breakbadhabits.android.app.rememberEpicViewModel
import breakbadhabits.entity.Habit
import breakbadhabits.presentation.CurrentHabitAbstinenceViewModel
import breakbadhabits.presentation.HabitIdsViewModel
import breakbadhabits.presentation.HabitViewModel
import breakbadhabits.ui.kit.Button
import breakbadhabits.ui.kit.Card
import breakbadhabits.ui.kit.Icon
import breakbadhabits.ui.kit.IconButton
import breakbadhabits.ui.kit.InteractionType
import breakbadhabits.ui.kit.Text
import breakbadhabits.ui.kit.Title
import epicarchitect.epicstore.compose.epicStoreItems
import epicarchitect.epicstore.compose.rememberEpicStoreEntry


@Composable
fun HabitsScreen(
    openHabit: (Habit.Id) -> Unit,
    openHabitEventCreation: (Habit.Id) -> Unit,
    openHabitCreation: () -> Unit,
    openSettings: () -> Unit
) {
    val appDependencies = LocalPresentationModule.current
    val habitIdsFeature = rememberEpicViewModel {
        appDependencies.habitIdsModule.createHabitIdsViewModel()
    }
    val habitIds by habitIdsFeature.state.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                Title(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.app_name)
                )

                IconButton(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = openSettings
                ) {
                    Icon(painterResource(R.drawable.ic_settings))
                }
            }

            val ids = habitIds
            if (ids is HabitIdsViewModel.State.NotExist) {
                Text(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.habits_empty)
                )
            } else if (ids is HabitIdsViewModel.State.Loaded) {
                LazyColumn(
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 4.dp,
                        bottom = 100.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    epicStoreItems(
                        items = ids.habitIds,
                        key = { it.value }
                    ) { habitId ->
                        HabitItem(
                            habitId = habitId,
                            onItemClick = {
                                openHabit(habitId)
                            },
                            onResetClick = {
                                openHabitEventCreation(habitId)
                            }
                        )
                    }
                }
            }
        }

        Button(
            modifier = Modifier
                .padding(24.dp)
                .align(Alignment.BottomCenter),
            onClick = {
                openHabitCreation()
            },
            text = stringResource(R.string.habits_newHabit),
            interactionType = InteractionType.MAIN
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LazyItemScope.HabitItem(
    habitId: Habit.Id,
    onItemClick: () -> Unit,
    onResetClick: () -> Unit
) {
    val appDependencies = LocalPresentationModule.current
    val habitIconResources = LocalHabitIconResources.current
    val abstinenceFormatter = LocalHabitAbstinenceFormatter.current
    val habitViewModel = rememberEpicViewModel {
        appDependencies.habitModule.createHabitViewModel(habitId)
    }
    val habitAbstinenceViewModel = rememberEpicStoreEntry {
        appDependencies.currentHabitAbstinenceModule.createCurrentHabitAbstinenceViewModel(habitId)
    }
    val habitState by habitViewModel.state.collectAsState()
    val abstinenceState by habitAbstinenceViewModel.state.collectAsState()

    when (val state = habitState) {
        is HabitViewModel.State.Loaded -> {
            val habit = state.habit
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItemPlacement()
            ) {
                Box(
                    modifier = Modifier.clickable { onItemClick() },
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
                            Icon(
                                painter = painterResource(habitIconResources[habit.iconResource.iconId]),
                            )
                            Title(
                                modifier = Modifier.padding(start = 12.dp),
                                text = habit.name.value
                            )
                        }
                        Row(
                            modifier = Modifier.padding(top = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_time)
                            )
                            Text(
                                modifier = Modifier.padding(start = 12.dp),
                                text = when (val state = abstinenceState) {
                                    is CurrentHabitAbstinenceViewModel.State.Loaded -> {
                                        abstinenceFormatter.format(state.abstinence)
                                    }
                                    is CurrentHabitAbstinenceViewModel.State.Loading -> "loading..."
                                    is CurrentHabitAbstinenceViewModel.State.NotExist -> stringResource(
                                        R.string.habit_noEvents
                                    )
                                }
                            )
                        }
                    }

                    IconButton(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(4.dp),
                        onClick = {
                            onResetClick()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_reset)
                        )
                    }
                }
            }
        }

        is HabitViewModel.State.Loading -> Text("Loading")
        is HabitViewModel.State.NotExist -> Text("Not exists")
    }
}