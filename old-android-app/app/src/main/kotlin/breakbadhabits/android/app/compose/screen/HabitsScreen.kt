package breakbadhabits.android.app.compose.screen

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
import breakbadhabits.android.app.R
import breakbadhabits.android.app.appDependencies
import breakbadhabits.android.app.formatter.AbstinenceTimeFormatter
import breakbadhabits.android.app.resources.HabitIconResources
import breakbadhabits.android.compose.ui.Button
import breakbadhabits.android.compose.ui.Card
import breakbadhabits.android.compose.ui.Icon
import breakbadhabits.android.compose.ui.IconButton
import breakbadhabits.android.compose.ui.InteractionType
import breakbadhabits.android.compose.ui.Text
import breakbadhabits.android.compose.ui.Title
import epicarchitect.epicstore.compose.epicStoreItems
import epicarchitect.epicstore.compose.rememberEpicStoreEntry


@Composable
fun HabitsScreen(
    openHabit: (habitId: Int) -> Unit,
    openHabitEventCreation: (habitId: Int) -> Unit,
    openHabitCreation: () -> Unit,
    openSettings: () -> Unit
) {
    val habitIdsFeature = rememberEpicStoreEntry {
        appDependencies.createHabitIdsFeature()
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

            if (habitIds.isEmpty()) {
                Text(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.habits_empty)
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 4.dp,
                        bottom = 100.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    epicStoreItems(habitIds, key = { it }) { habitId ->
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
    habitId: Int,
    onItemClick: () -> Unit,
    onResetClick: () -> Unit
) {
    val habitIconResources: HabitIconResources = appDependencies.habitIconResources
    val abstinenceTimeFormatter: AbstinenceTimeFormatter = appDependencies.abstinenceTimeFormatter
    val habitNameFeature = rememberEpicStoreEntry {
        appDependencies.createHabitNameFeature(habitId)
    }
    val habitIconIdFeature = rememberEpicStoreEntry {
        appDependencies.createHabitIconIdFeature(habitId)
    }
    val habitAbstinenceTimeFeature = rememberEpicStoreEntry {
        appDependencies.createHabitAbstinenceTimeFeature(habitId)
    }

    val habitName by habitNameFeature.state.collectAsState()
    val habitIconId by habitIconIdFeature.collectAsState()
    val habitAbstinenceTime by habitAbstinenceTimeFeature.state.collectAsState()

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
                        painter = painterResource(
                            habitIconId?.let {
                                habitIconResources[it]
                            } ?: R.drawable.ic_awesome
                        ),
                    )
                    Title(
                        modifier = Modifier.padding(start = 12.dp),
                        text = habitName ?: ""
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
                        text = when (val time = habitAbstinenceTime) {
                            null -> stringResource(R.string.habits_noEvents)
                            else -> abstinenceTimeFormatter.format(time)
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