package breakbadhabits.app.android

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
import androidx.lifecycle.viewmodel.compose.viewModel
import breakbadhabits.app.entity.Habit
import breakbadhabits.foundation.uikit.DataFlowBox
import breakbadhabits.foundation.uikit.button.Button
import breakbadhabits.foundation.uikit.Icon
import breakbadhabits.foundation.uikit.IconButton
import breakbadhabits.foundation.uikit.button.InteractionType
import breakbadhabits.foundation.uikit.text.Text
import breakbadhabits.foundation.uikit.text.Title

@Composable
fun HabitScreen(
    habitId: Habit.Id,
    openHabitEventCreation: () -> Unit,
    openHabitEventEditing: (habitEventId: Int) -> Unit,
    openHabitEditing: () -> Unit,
    showALlEvents: () -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val habitViewModel = viewModel {
        presentationModule.createHabitDetailsViewModel(habitId)
    }

    DataFlowBox(habitViewModel.habitController) {
        if (it == null) {
            Text("Not exist")
        } else {
            LoadedScreen(habitId, it)
        }
    }
}

@Composable
private fun LoadedScreen(
    habitId: Habit.Id,
    habit: Habit
) {
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
                    painter = painterResource(habitIconResources[habit.iconResource.iconId])
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

//fun main() {
//    println("testCalculateByDays")
//    println()
//    testCalculateByDays()
//    println("testCalculateByHours")
//    println()
//    testCalculateByHours()
//}
//
//fun testCalculateByHours() {
//    calculateByPerHour(
//        rangeHourDistance = 30,
//        eventsPerHour = 5
//    )
//    calculateByPerHour(
//        rangeHourDistance = 3,
//        eventsPerHour = 3
//    )
//    calculateByPerHour(
//        rangeHourDistance = 1,
//        eventsPerHour = 60
//    )
//}
//
//fun testCalculateByDays() {
//    calculateByPerDays(
//        rangeDayDistance = 30,
//        eventsPerDay = 5
//    )
//    calculateByPerDays(
//        rangeDayDistance = 333,
//        eventsPerDay = 3
//    )
//    calculateByPerDays(
//        rangeDayDistance = 1,
//        eventsPerDay = 5
//    )
//}
//
//private fun calculateByPerHour(
//    rangeHourDistance: Int,
//    eventsPerHour: Int
//) {
//    rangeHourDistance.print("initial rangeHourDistance")
//    eventsPerHour.print("initial eventsPerHour")
//
//    val minutesInHour = 60
//    val rangeMinuteDistance = minutesInHour * rangeHourDistance
//    val eventsPerMinute = eventsPerHour.toDouble() / rangeMinuteDistance
//
//    val backRangeDayDistance = rangeMinuteDistance / minutesInHour
//    val backEventsPerDay = eventsPerMinute * rangeMinuteDistance
//
//    rangeMinuteDistance.print("converted rangeMinuteDistance")
//    eventsPerMinute.print("converted eventsPerMinute")
//
//    backRangeDayDistance.print("back eangeMinuteDistance")
//    backEventsPerDay.print("back eventsPerMinute")
//
//    println()
//}
//
//private fun calculateByPerDays(
//    rangeDayDistance: Int,
//    eventsPerDay: Int
//) {
//    rangeDayDistance.print("initial rangeDayDistance")
//    eventsPerDay.print("initial eventsPerDay")
//
//    val minutesInDay = 24 * 60
//    val rangeMinuteDistance = minutesInDay * rangeDayDistance
//    val eventsPerMinute = eventsPerDay.toDouble() / rangeMinuteDistance
//
//    val backRangeDayDistance = rangeMinuteDistance / minutesInDay
//    val backEventsPerDay = eventsPerMinute * rangeMinuteDistance
//
//    rangeMinuteDistance.print("converted rangeMinuteDistance")
//    eventsPerMinute.print("converted eventsPerMinute")
//
//    backRangeDayDistance.print("back eangeMinuteDistance")
//    backEventsPerDay.print("back eventsPerMinute")
//
//    println()
//}
//
//private fun Any.print(title: String) {
//    val string = when (this) {
//        is Double -> {
//            String.format("%.12f", this)
//        }
//        else -> toString()
//    }
//    println("$title: $string")
//}