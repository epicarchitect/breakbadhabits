package breakbadhabits.android.app.ui.habits

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import breakbadhabits.android.app.R
import breakbadhabits.android.app.ui.LocalHabitIconResources
import breakbadhabits.android.app.ui.LocalPresentationModule
import breakbadhabits.app.entity.Habit
import breakbadhabits.foundation.controller.LoadingController
import breakbadhabits.foundation.uikit.LoadingBox
import breakbadhabits.foundation.uikit.Icon
import breakbadhabits.foundation.uikit.IconButton
import breakbadhabits.foundation.uikit.button.Button
import breakbadhabits.foundation.uikit.button.InteractionType
import breakbadhabits.foundation.uikit.text.Text
import breakbadhabits.foundation.uikit.text.Title

@Composable
fun HabitDetailsScreen(
    habitController: LoadingController<Habit?>
) {
    LoadingBox(habitController) {
        if (it == null) {
            Text("Not exist")
        } else {
            LoadedScreen(it)
        }
    }
}

@Composable
private fun LoadedScreen(habit: Habit) {
    val habitIconResources = LocalHabitIconResources.current
    val appDependencies = LocalPresentationModule.current
//    val habitAbstinenceViewModel = viewModel {
//        appDependencies.createCurrentHabitAbstinenceViewModel(habitId)
//    }
//    val abstinenceState by habitAbstinenceViewModel.state.collectAsState()

//    val habitDeletionViewModel = viewModel {
//        appDependencies.createHabitIdsViewModel(habitId)
//    }
//    val habitDeletionState by habitDeletionViewModel.state.collectAsState()

//    if (habitDeletionState is HabitDeletionViewModel.State.Confirming) {
//        Dialog(onDismissRequest = habitDeletionViewModel::cancelConfirming) {
//            Card {
//                Column(
//                    modifier = Modifier.padding(16.dp)
//                ) {
//                    Text(stringResource(R.string.habit_deleteConfirmation))
//
//                    Row(
//                        modifier = Modifier.align(Alignment.End),
//                        horizontalArrangement = Arrangement.spacedBy(16.dp)
//                    ) {
//                        Button(
//                            text = stringResource(R.string.cancel),
//                            onClick = habitDeletionViewModel::cancelConfirming,
//                            elevation = 0.dp
//                        )
//                        Button(
//                            text = stringResource(R.string.yes),
//                            onClick = habitDeletionViewModel::confirm,
//                            elevation = 0.dp
//                        )
//                    }
//                }
//            }
//        }
//    }

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
                    painter = painterResource(habitIconResources[habit.icon.iconId])
                )

                Title(
                    modifier = Modifier.padding(top = 8.dp),
                    text = habit.name.value
                )

//                Text(
//                    text = when (val state = abstinenceState) {
//                        is CurrentHabitAbstinenceViewModel.State.Loaded -> state.abstinence.range.toString()
//                        is CurrentHabitAbstinenceViewModel.State.Loading -> "loading..."
//                        is CurrentHabitAbstinenceViewModel.State.NotExist -> stringResource(R.string.habit_noEvents)
//                    }
//                )

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
                    onClick = {
//                        habitDeletionViewModel::startDeletion
                    },
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