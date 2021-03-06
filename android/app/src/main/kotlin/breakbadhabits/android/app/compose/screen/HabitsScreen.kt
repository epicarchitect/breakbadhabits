package breakbadhabits.android.app.compose.screen

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
import androidx.compose.foundation.lazy.items
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
import breakbadhabits.android.app.formatter.AbstinenceTimeFormatter
import breakbadhabits.android.app.resources.HabitIconResources
import breakbadhabits.android.app.utils.TikTik
import breakbadhabits.android.app.viewmodel.HabitsViewModel
import breakbadhabits.android.compose.ui.InteractionType
import breakbadhabits.android.compose.ui.Button
import breakbadhabits.android.compose.ui.Card
import breakbadhabits.android.compose.ui.Icon
import breakbadhabits.android.compose.ui.IconButton
import breakbadhabits.android.compose.ui.Text
import breakbadhabits.android.compose.ui.Title

@Composable
fun HabitsScreen(
    habitsViewModel: HabitsViewModel,
    habitIconResources: HabitIconResources,
    abstinenceTimeFormatter: AbstinenceTimeFormatter,
    openHabit: (habitId: Int) -> Unit,
    openHabitEventCreation: (habitId: Int) -> Unit,
    openHabitCreation: () -> Unit,
    openSettings: () -> Unit
) {
    val habitState by habitsViewModel.habitsState.collectAsState()
    val habits = habitState

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
                    modifier = Modifier
                        .align(Alignment.Center),
                    text = stringResource(R.string.app_name)
                )

                IconButton(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = openSettings
                ) {
                    Icon(painterResource(R.drawable.ic_settings))
                }
            }

            if (habits != null) {
                if (habits.isEmpty()) {
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
                        items(habits) { habit ->
                            HabitItem(
                                habit = habit,
                                habitIconResources = habitIconResources,
                                abstinenceTimeFormatter = abstinenceTimeFormatter,
                                onItemClick = {
                                    openHabit(habit.habitId)
                                },
                                onResetClick = {
                                    openHabitEventCreation(habit.habitId)
                                }
                            )
                        }
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
            text =  stringResource(R.string.habits_newHabit),
            interactionType = InteractionType.MAIN
        )
    }
}

@Composable
private fun HabitItem(
    habit: HabitsViewModel.Habit,
    habitIconResources: HabitIconResources,
    abstinenceTimeFormatter: AbstinenceTimeFormatter,
    onItemClick: () -> Unit,
    onResetClick: () -> Unit
) {
    val lastHabitEvent by habit.lastHabitEvent.collectAsState(initial = null)
    val currentTime by TikTik.everySecond().collectAsState(initial = System.currentTimeMillis())

    Card(
        modifier = Modifier.fillMaxWidth()
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
                        painter = painterResource(habitIconResources[habit.habitIconId]),
                    )
                    Title(
                        modifier = Modifier.padding(start = 12.dp),
                        text = habit.habitName
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
                        text = when (val event = lastHabitEvent) {
                            null ->  stringResource(R.string.habits_noEvents)
                            else -> abstinenceTimeFormatter.format(currentTime - event.time)
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