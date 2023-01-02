package breakbadhabits.android.app.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import breakbadhabits.android.app.LocalPresentationModule
import breakbadhabits.android.app.R
import breakbadhabits.android.app.rememberEpicViewModel
import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitTrack
import breakbadhabits.presentation.HabitTrackCreationViewModel
import breakbadhabits.presentation.HabitViewModel
import breakbadhabits.ui.kit.Button
import breakbadhabits.ui.kit.Text
import breakbadhabits.ui.kit.TextField
import breakbadhabits.ui.kit.Title

@Composable
fun HabitTrackCreationScreen(
    habitId: Habit.Id,
    onFinished: () -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val habitTrackCreationViewModel = rememberEpicViewModel {
        presentationModule.habitTrackCreationModule.createHabitTrackCreationViewModel(habitId)
    }

    val habitViewModel = rememberEpicViewModel {
        presentationModule.habitModule.createHabitViewModel(habitId)
    }

    val habitTrackCreationViewModelState by habitTrackCreationViewModel.state.collectAsState()
    val habitViewModelState by habitViewModel.state.collectAsState()

    when (val habitState = habitViewModelState) {
        is HabitViewModel.State.Loaded -> {
            when (val habitTrackCreationState = habitTrackCreationViewModelState) {
                is HabitTrackCreationViewModel.State.Input -> {
                    InputScreen(
                        habitState = habitState,
                        trackCreationState = habitTrackCreationState,
                        onCommentChanged = habitTrackCreationViewModel::updateComment,

                    )
                }

                is HabitTrackCreationViewModel.State.Created -> {
                    Text("created")
                }

                is HabitTrackCreationViewModel.State.Creating -> {
                    Text("creating")
                }
            }
        }

        else -> {
            Text("loading")
        }
    }
}

@Composable
private fun InputScreen(
    habitState: HabitViewModel.State.Loaded,
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