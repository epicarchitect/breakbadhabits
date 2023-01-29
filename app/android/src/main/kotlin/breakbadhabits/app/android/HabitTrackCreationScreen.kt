package breakbadhabits.app.android

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.presentation.HabitTrackCreationViewModel

@Composable
fun HabitTrackCreationScreen(
    habitId: Habit.Id,
    onFinished: () -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val habitTrackCreationViewModel = viewModel {
        presentationModule.createHabitTrackCreationViewModel(habitId)
    }

//    val habitViewModel = rememberEpicViewModel {
//        presentationModule.habitModule.createHabitViewModel(habitId)
//    }

//    val habitTrackCreationViewModelState by habitTrackCreationViewModel.state.collectAsState()
//    val habitViewModelState by habitViewModel.state.collectAsState()
//    when (val habitTrackCreationState = habitTrackCreationViewModelState) {
//        is HabitTrackCreationViewModel.State.Input -> {
//            InputScreen(
//                trackCreationState = habitTrackCreationState,
//                onCommentChanged = habitTrackCreationViewModel::updateComment,
//
//                )
//        }
//
//        is HabitTrackCreationViewModel.State.Created -> {
//            Text("created")
//        }
//
//        is HabitTrackCreationViewModel.State.Creating -> {
//            Text("creating")
//        }
//    }
}

@Composable
private fun InputScreen(
    trackCreationState: HabitTrackCreationViewModel.State.Input,
    onCommentChanged: (HabitTrack.Comment) -> Unit,
) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(rememberScrollState())
//    ) {
//        Title(
//            modifier = Modifier.padding(start = 18.dp, top = 18.dp, end = 18.dp, bottom = 4.dp),
//            text = stringResource(R.string.habitEventCreation_title)
//        )
//
//        Text(
//            modifier = Modifier.padding(start = 16.dp, top = 4.dp, end = 16.dp),
//            text = stringResource(
//                R.string.habitEventCreation_habitName,
//                habitState.habit.name ?: ""
//            )
//        )
//
//        Text(
//            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
//            text = stringResource(R.string.habitEventCreation_event_description)
//        )
//
//        Button(
//            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
//            onClick = {
//
//            },
//            text = stringResource(
//                R.string.habitEventCreation_eventDate,
//                "asd"
//            )
//        )
//
//        Button(
//            modifier = Modifier.padding(start = 16.dp, top = 2.dp, end = 16.dp),
//            onClick = {
//
//            },
//            text = stringResource(
//                R.string.habitEventCreation_eventTime,
//                "asdasd"
//            )
//        )
//
//        Text(
//            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
//            text = stringResource(R.string.habitEventCreation_comment_description)
//        )
//
//        TextField(
//            modifier = Modifier
//                .padding(start = 16.dp, top = 8.dp, end = 16.dp)
//                .fillMaxWidth(),
//            value = trackCreationState.comment?.value ?: "",
//            onValueChange = commentInputFeature::changeInput,
//            label = stringResource(R.string.habitEventCreation_comment)
//        )
//
//        Spacer(modifier = Modifier.weight(1.0f))
//
//        Text(
//            modifier = Modifier
//                .padding(start = 16.dp, end = 16.dp, top = 32.dp)
//                .align(Alignment.End),
//            text = stringResource(R.string.habitEventCreation_finish_description)
//        )
//
//        Button(
//            modifier = Modifier
//                .padding(16.dp)
//                .align(Alignment.End),
//            enabled = true,
//            onClick = {
//                creationFeature.startCreation(
//                    habitId,
//                    time!!,
//                    comment
//                )
//                onFinished()
//            },
//            text = stringResource(R.string.habitEventCreation_finish),
//            interactionType = InteractionType.MAIN
//        )
//    }
}