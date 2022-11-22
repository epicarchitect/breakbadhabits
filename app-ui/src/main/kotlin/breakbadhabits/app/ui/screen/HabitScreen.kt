package breakbadhabits.app.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import breakbadhabits.app.ui.LocalAppDependencies
import breakbadhabits.app.ui.LocalHabitIconResources
import breakbadhabits.app.ui.R
import breakbadhabits.entity.Habit
import breakbadhabits.feature.habits.presentation.CurrentHabitAbstinenceEpicViewModel
import breakbadhabits.feature.habits.presentation.HabitViewModel
import breakbadhabits.ui.kit.Button
import breakbadhabits.ui.kit.Icon
import breakbadhabits.ui.kit.IconButton
import breakbadhabits.ui.kit.InteractionType
import breakbadhabits.ui.kit.Text
import breakbadhabits.ui.kit.Title
import epicarchitect.epicstore.compose.rememberEpicStoreEntry

@Composable
fun HabitScreen(
    habitId: Habit.Id,
    openHabitEventCreation: () -> Unit,
    openHabitEventEditing: (habitEventId: Int) -> Unit,
    openHabitEditing: () -> Unit,
    showALlEvents: () -> Unit
) {
    val appDependencies = LocalAppDependencies.current
    val habitViewModel = rememberEpicStoreEntry {
        appDependencies.habitsFeatureFactory.createHabitViewModel(habitId)
    }

    val habitState by habitViewModel.state.collectAsState()

    when (val state = habitState) {
        is HabitViewModel.State.Loaded -> LoadedScreen(habitId, state)
        is HabitViewModel.State.Loading -> Text("Loading")
        is HabitViewModel.State.NotExist -> Text("Not exist")
    }
}

@Composable
private fun LoadedScreen(
    habitId: Habit.Id,
    state: HabitViewModel.State.Loaded
) {
    val habitIconResources = LocalHabitIconResources.current
    val appDependencies = LocalAppDependencies.current
    val habitAbstinenceViewModel = rememberEpicStoreEntry {
        appDependencies.habitsFeatureFactory.createHabitCurrentAbstinenceViewModel(habitId)
    }
    val abstinenceState by habitAbstinenceViewModel.state.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier.size(44.dp),
                    painter = painterResource(habitIconResources[state.habit.iconResource.iconId])
                )

                Title(
                    modifier = Modifier.padding(top = 8.dp),
                    text = state.habit.name.value
                )

                Text(
                    text = when (val state = abstinenceState) {
                        is CurrentHabitAbstinenceEpicViewModel.State.Loaded -> state.abstinence.interval.toString()
                        is CurrentHabitAbstinenceEpicViewModel.State.Loading -> "loading..."
                        is CurrentHabitAbstinenceEpicViewModel.State.NotExist -> stringResource(R.string.habit_noEvents)
                    }
                )

                Button(
                    modifier = Modifier.padding(top = 8.dp),
                    onClick = {
                        //openHabitEventCreation()
                    },
                    text = stringResource(R.string.habit_resetTime),
                    interactionType = InteractionType.MAIN
                )
            }

            IconButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = {
//                    openHabitEditing()
                }
            ) {
                Icon(painterResource(R.drawable.ic_settings))
            }
        }
    }
}