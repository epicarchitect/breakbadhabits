package breakbadhabits.android.app.ui.habits

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import breakbadhabits.android.app.ui.LocalDateTimeFormatter
import breakbadhabits.android.app.ui.LocalHabitIconResourceProvider
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitAbstinence
import breakbadhabits.foundation.controller.LoadingController
import breakbadhabits.foundation.uikit.Icon
import breakbadhabits.foundation.uikit.IconButton
import breakbadhabits.foundation.uikit.LoadingBox
import breakbadhabits.foundation.uikit.button.Button
import breakbadhabits.foundation.uikit.button.InteractionType
import breakbadhabits.foundation.uikit.text.Text
import breakbadhabits.foundation.uikit.text.Title

@Composable
fun HabitDetailsScreen(
    habitController: LoadingController<Habit?>,
    habitAbstinenceController: LoadingController<HabitAbstinence?>,
    onEditClick: () -> Unit,
    onAddTrackClick: () -> Unit,
) {
    val habitIconResources = LocalHabitIconResourceProvider.current
    val dateTimeFormatter = LocalDateTimeFormatter.current

    LoadingBox(habitController) { habit ->
        if (habit == null) {
            Text("Not exist")
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Icon(
                    modifier = Modifier
                        .size(44.dp)
                        .align(Alignment.CenterHorizontally),
                    painter = painterResource(habitIconResources[habit.icon].resourceId)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Title(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = habit.name.value
                )

                Spacer(modifier = Modifier.height(8.dp))

                LoadingBox(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    controller = habitAbstinenceController
                ) { abstinence ->
                    Text(
                        text = abstinence?.let {
                            dateTimeFormatter.formatDistance(it.range.value)
                        } ?: stringResource(R.string.habits_noEvents)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = onAddTrackClick,
                    text = stringResource(R.string.habit_resetTime),
                    interactionType = InteractionType.MAIN
                )
            }
        }

        IconButton(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopEnd),
            onClick = onEditClick
        ) {
            Icon(painterResource(R.drawable.ic_settings))
        }
    }
}