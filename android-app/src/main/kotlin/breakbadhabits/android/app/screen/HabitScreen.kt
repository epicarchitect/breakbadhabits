package breakbadhabits.android.app.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.window.Dialog
import breakbadhabits.android.app.LocalHabitIconResources
import breakbadhabits.android.app.LocalPresentationModule
import breakbadhabits.android.app.R
import breakbadhabits.android.app.rememberEpicViewModel
import breakbadhabits.entity.Habit
import breakbadhabits.presentation.CurrentHabitAbstinenceViewModel
import breakbadhabits.presentation.HabitDeletionViewModel
import breakbadhabits.presentation.HabitViewModel
import breakbadhabits.ui.kit.Button
import breakbadhabits.ui.kit.Card
import breakbadhabits.ui.kit.Icon
import breakbadhabits.ui.kit.IconButton
import breakbadhabits.ui.kit.InteractionType
import breakbadhabits.ui.kit.Text
import breakbadhabits.ui.kit.Title

@Composable
fun HabitScreen(
    habitId: Habit.Id,
    openHabitEventCreation: () -> Unit,
    openHabitEventEditing: (habitEventId: Int) -> Unit,
    openHabitEditing: () -> Unit,
    showALlEvents: () -> Unit
) {
    val appDependencies = LocalPresentationModule.current
    val habitViewModel = rememberEpicViewModel {
        appDependencies.habitModule.createHabitViewModel(habitId)
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
    val appDependencies = LocalPresentationModule.current
    val habitAbstinenceViewModel = rememberEpicViewModel {
        appDependencies.currentHabitAbstinenceModule.createCurrentHabitAbstinenceViewModel(habitId)
    }
    val abstinenceState by habitAbstinenceViewModel.state.collectAsState()

    val habitDeletionViewModel = rememberEpicViewModel {
        appDependencies.habitDeletionModule.createHabitIdsViewModel(habitId)
    }
    val habitDeletionState by habitDeletionViewModel.state.collectAsState()

    if (habitDeletionState is HabitDeletionViewModel.State.Confirming) {
        Dialog(onDismissRequest = habitDeletionViewModel::cancelConfirming) {
            Card {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(stringResource(R.string.habit_deleteConfirmation))

                    Row(
                        modifier = Modifier.align(Alignment.End),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            text = stringResource(R.string.cancel),
                            onClick = habitDeletionViewModel::cancelConfirming,
                            elevation = 0.dp
                        )
                        Button(
                            text = stringResource(R.string.yes),
                            onClick = habitDeletionViewModel::confirm,
                            elevation = 0.dp
                        )
                    }
                }
            }
        }
    }

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
                        is CurrentHabitAbstinenceViewModel.State.Loaded -> state.abstinence.range.toString()
                        is CurrentHabitAbstinenceViewModel.State.Loading -> "loading..."
                        is CurrentHabitAbstinenceViewModel.State.NotExist -> stringResource(R.string.habit_noEvents)
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

                Button(
                    modifier = Modifier.padding(top = 8.dp),
                    onClick = habitDeletionViewModel::startDeletion,
                    text = "Delete",
                    interactionType = InteractionType.DANGEROUS
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