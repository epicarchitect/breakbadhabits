package epicarchitect.breakbadhabits.screens.habits.records.editing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.Environment
import epicarchitect.breakbadhabits.database.HabitEventRecord
import epicarchitect.breakbadhabits.habits.timeRange
import epicarchitect.breakbadhabits.habits.validation.HabitEventCountError
import epicarchitect.breakbadhabits.habits.validation.HabitEventRecordTimeRangeError
import epicarchitect.breakbadhabits.habits.validation.checkHabitEventCount
import epicarchitect.breakbadhabits.habits.validation.checkHabitEventRecordTimeRange
import epicarchitect.breakbadhabits.uikit.DateTimeRangeInputCard
import epicarchitect.breakbadhabits.uikit.Dialog
import epicarchitect.breakbadhabits.uikit.FlowStateContainer
import epicarchitect.breakbadhabits.uikit.SimpleScrollableScreen
import epicarchitect.breakbadhabits.uikit.button.Button
import epicarchitect.breakbadhabits.uikit.button.ButtonStyles
import epicarchitect.breakbadhabits.uikit.regex.Regexps
import epicarchitect.breakbadhabits.uikit.stateOfOneOrNull
import epicarchitect.breakbadhabits.uikit.text.Text
import epicarchitect.breakbadhabits.uikit.text.TextInputCard

class HabitEventRecordEditingScreen(private val habitEventRecordId: Int) : Screen {
    @Composable
    override fun Content() {
        HabitEventRecordEditing(habitEventRecordId)
    }
}

class HabitEventRecordEditingState(
    initialRecord: HabitEventRecord
) {
    var timeRange by mutableStateOf(initialRecord.timeRange())
    var timeRangeError by mutableStateOf<HabitEventRecordTimeRangeError?>(null)
    var eventCount by mutableIntStateOf(initialRecord.eventCount)
    var eventCountError by mutableStateOf<HabitEventCountError?>(null)
    var comment by mutableStateOf(initialRecord.comment)
}

// TODO: should be savable
@Composable
fun rememberHabitEventRecordEditingState(
    initialRecord: HabitEventRecord
) = remember(initialRecord) {
    HabitEventRecordEditingState(initialRecord)
}

@Composable
fun HabitEventRecordEditing(habitEventRecordId: Int) {
    val strings = Environment.resources.strings.habitEventRecordEditingStrings
    val navigator = LocalNavigator.currentOrThrow

    FlowStateContainer(
        state = stateOfOneOrNull {
            Environment.database.habitEventRecordQueries.recordById(habitEventRecordId)
        }
    ) { habitEventRecord ->
        if (habitEventRecord != null) {
            FlowStateContainer(
                state = stateOfOneOrNull(habitEventRecord) {
                    Environment.database.habitQueries.habitById(habitEventRecord.habitId)
                }
            ) { habit ->
                SimpleScrollableScreen(
                    title = strings.titleText(habit?.name ?: "..."),
                    onBackClick = navigator::pop
                ) {
                    if (habit != null) {
                        Content(habitEventRecord)
                    }
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.Content(initialRecord: HabitEventRecord) {
    val strings = Environment.resources.strings.habitEventRecordEditingStrings
    val habitEventRecordQueries = Environment.database.habitEventRecordQueries
    val navigator = LocalNavigator.currentOrThrow
    val state = rememberHabitEventRecordEditingState(initialRecord)

    val timeZone = Environment.dateTime.currentTimeZone()
    var showDeletion by remember { mutableStateOf(false) }

    if (showDeletion) {
        DeletionDialog(
            record = initialRecord,
            onDismiss = { showDeletion = false }
        )
    }

    Spacer(Modifier.height(16.dp))

    TextInputCard(
        title = strings.eventCountTitle(),
        description = strings.eventCountDescription(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        value = state.eventCount.toString(),
        onValueChange = {
            state.eventCount = it.toIntOrNull() ?: 0
            state.eventCountError = null
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        regex = Regexps.integersOrEmpty(maxCharCount = 4),
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
            state.timeRange = it
        },
        startTimeLabel = strings.startDateTimeLabel(),
        endTimeLabel = strings.endDateTimeLabel(),
        timeZone = timeZone
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

    Button(
        modifier = Modifier.padding(horizontal = 16.dp),
        text = strings.deleteButton(),
        style = ButtonStyles.dangerous,
        onClick = {
            showDeletion = true
        }
    )

    Spacer(modifier = Modifier.weight(1.0f))

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .align(Alignment.End),
        text = strings.finishButton(),
        style = ButtonStyles.primary,
        onClick = {
            state.eventCountError = checkHabitEventCount(state.eventCount)
            if (state.eventCountError != null) return@Button

            state.timeRangeError = checkHabitEventRecordTimeRange(
                timeRange = state.timeRange,
                currentTime = Environment.dateTime.currentInstant()
            )
            if (state.timeRangeError != null) return@Button

            habitEventRecordQueries.update(
                id = initialRecord.id,
                startTime = state.timeRange.start,
                endTime = state.timeRange.endInclusive,
                eventCount = state.eventCount,
                comment = state.comment
            )
            navigator.pop()
        }
    )

    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun DeletionDialog(
    record: HabitEventRecord,
    onDismiss: () -> Unit
) {
    val strings = Environment.resources.strings.habitEventRecordEditingStrings
    val habitEventRecordQueries = Environment.database.habitEventRecordQueries
    val navigator = LocalNavigator.currentOrThrow

    Dialog(onDismiss) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = strings.deleteConfirmation(),
                type = Text.Type.Description,
                priority = Text.Priority.High
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.align(Alignment.End)
            ) {
                Button(
                    text = strings.cancel(),
                    onClick = onDismiss
                )

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    text = strings.yes(),
                    style = ButtonStyles.primary,
                    onClick = {
                        habitEventRecordQueries.deleteById(record.id)
                        navigator.pop()
                    }
                )
            }
        }
    }
}