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
    habitController: LoadingController<Habit?>,
    onEditClick: () -> Unit,
    onAddTrackClick: () -> Unit,
) {
    LoadingBox(habitController) {
        if (it == null) {
            Text("Not exist")
        } else {
            LoadedScreen(
                habit = it,
                onEditClick = onEditClick,
                onAddTrackClick = onAddTrackClick
            )
        }
    }
}

@Composable
private fun LoadedScreen(
    habit: Habit,
    onEditClick: () -> Unit,
    onAddTrackClick: () -> Unit,
) {
    val habitIconResources = LocalHabitIconResources.current

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

                Button(
                    modifier = Modifier.padding(top = 8.dp),
                    onClick = onAddTrackClick,
                    text = stringResource(R.string.habit_resetTime),
                    interactionType = InteractionType.MAIN
                )
            }

            IconButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = onEditClick
            ) {
                Icon(painterResource(R.drawable.ic_settings))
            }
        }
    }
}