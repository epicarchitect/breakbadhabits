package epicarchitect.breakbadhabits.screens.habits.eventRecords.editing

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.environment.LocalAppEnvironment
import epicarchitect.breakbadhabits.habits.checkHabitEventCount
import epicarchitect.breakbadhabits.habits.checkHabitEventRecordTimeRange
import epicarchitect.breakbadhabits.math.ranges.ascended
import epicarchitect.breakbadhabits.screens.root.LocalRootNavController
import epicarchitect.breakbadhabits.uikit.DateTimeRangeInputCard
import epicarchitect.breakbadhabits.uikit.SimpleScrollableScreen
import epicarchitect.breakbadhabits.uikit.TextInputCard


@Composable
fun HabitEventRecordEditingScreen(
    habitEventRecordId: Int?,
    habitId: Int
) {
    val state = rememberHabitEventRecordEditingState(
        eventRecordId = habitEventRecordId,
        habitId = habitId
    ) ?: return

    val environment = LocalAppEnvironment.current
    val navController = LocalRootNavController.current
    val habitRules = environment.habits.rules
    val strings = environment.resources.strings.habitEventRecordEditingStrings
    val habitEventRecordQueries = environment.database.habitEventRecordQueries
    val timeZone = environment.dateTime.currentTimeZone()

    var showDeletion by remember { mutableStateOf(false) }

    if (showDeletion) {
        HabitEventRecordDeletionDialog(
            recordId = state.initialRecord!!.id,
            onDismiss = { showDeletion = false },
            onDeleted = navController::popBackStack
        )
    }
    SimpleScrollableScreen(
        title = strings.titleText(
            isNewRecord = state.initialRecord == null,
            habitName = state.habit.name
        ),
        onBackClick = navController::popBackStack
    ) {

        Spacer(Modifier.height(16.dp))

        TextInputCard(
            title = strings.eventCountTitle(),
            description = strings.eventCountDescription(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            value = state.eventCount.let {
                if (it == 0) ""
                else it.toString()
            },
            onValueChange = {
                val value = it.toIntOrNull() ?: 0
                if (value <= habitRules.maxEventCount) {
                    state.eventCount = value
                    state.eventCountError = null
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            error = state.eventCountError?.let(strings::eventCountError)
        )

        Spacer(Modifier.height(16.dp))

        DateTimeRangeInputCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            title = strings.timeRangeTitle(),
            description = strings.timeRangeDescription(),
            error = state.timeRangeError?.let(strings::timeRangeError),
            value = state.timeRange,
            onChanged = {
                state.timeRange = it.ascended()
                state.timeRangeError = null
            },
            startTimeLabel = strings.startDateTimeLabel(),
            endTimeLabel = strings.endDateTimeLabel(),
            timeZone = timeZone,
            showAsRangeMode = state.dateTimeInputAsRange,
            onRangeModeChanged = { state.dateTimeInputAsRange = it }
        )

        Spacer(Modifier.height(16.dp))

        TextInputCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            title = strings.commentTitle(),
            description = strings.commentDescription(),
            value = state.comment,
            onValueChange = {
                state.comment = it
            },
            multiline = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (state.initialRecord != null) {
            OutlinedButton(
                modifier = Modifier.padding(horizontal = 16.dp),
                onClick = {
                    showDeletion = true
                }
            ) {
                Text(
                    text = strings.deleteButton()
                )
            }
        }

        Spacer(modifier = Modifier.weight(1.0f))

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.End),
            onClick = {
                state.eventCountError = checkHabitEventCount(state.eventCount)
                if (state.eventCountError != null) return@Button

                state.timeRangeError = checkHabitEventRecordTimeRange(
                    timeRange = state.timeRange,
                    currentTime = environment.dateTime.currentInstant()
                )
                if (state.timeRangeError != null) return@Button

                if (state.initialRecord == null) {
                    habitEventRecordQueries.insert(
                        habitId = state.habit.id,
                        startTime = state.timeRange.start,
                        endTime = state.timeRange.endInclusive,
                        eventCount = state.eventCount,
                        comment = state.comment
                    )
                } else {
                    habitEventRecordQueries.update(
                        id = state.initialRecord.id,
                        startTime = state.timeRange.start,
                        endTime = state.timeRange.endInclusive,
                        eventCount = state.eventCount,
                        comment = state.comment
                    )
                }

                navController.popBackStack()
            }
        ) {
            Text(
                text = strings.finishButton()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}