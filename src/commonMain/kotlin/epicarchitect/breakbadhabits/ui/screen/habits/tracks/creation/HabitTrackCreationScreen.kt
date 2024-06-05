package epicarchitect.breakbadhabits.ui.screen.habits.tracks.creation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.data.AppData
import epicarchitect.breakbadhabits.data.Habit
import epicarchitect.breakbadhabits.operation.datetime.withTime
import epicarchitect.breakbadhabits.operation.datetime.yesterday
import epicarchitect.breakbadhabits.operation.habits.totalHabitTrackEventCountByDaily
import epicarchitect.breakbadhabits.operation.habits.validation.HabitTrackEventCountIncorrectReason
import epicarchitect.breakbadhabits.operation.habits.validation.HabitTrackTimeRangeIncorrectReason
import epicarchitect.breakbadhabits.operation.habits.validation.habitTrackEventCountIncorrectReason
import epicarchitect.breakbadhabits.operation.habits.validation.habitTrackTimeRangeIncorrectReason
import epicarchitect.breakbadhabits.ui.component.FlowStateContainer
import epicarchitect.breakbadhabits.ui.component.SimpleTopAppBar
import epicarchitect.breakbadhabits.ui.component.SingleSelectionChipRow
import epicarchitect.breakbadhabits.ui.component.button.Button
import epicarchitect.breakbadhabits.ui.component.calendar.RangeSelectionCalendarDialog
import epicarchitect.breakbadhabits.ui.component.calendar.rememberSelectionCalendarState
import epicarchitect.breakbadhabits.ui.component.regex.Regexps
import epicarchitect.breakbadhabits.ui.component.stateOfOneOrNull
import epicarchitect.breakbadhabits.ui.component.text.ErrorText
import epicarchitect.breakbadhabits.ui.component.text.Text
import epicarchitect.breakbadhabits.ui.component.text.TextField
import epicarchitect.breakbadhabits.ui.format.formatted
import epicarchitect.breakbadhabits.ui.screen.habits.tracks.editing.dateSelectionMonthRange
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class HabitTrackCreationScreen(private val habitId: Int) : Screen {
    @Composable
    override fun Content() {
        HabitTrackCreation(habitId)
    }
}

@Composable
fun HabitTrackCreation(habitId: Int) {
    FlowStateContainer(
        state = stateOfOneOrNull {
            AppData.database.habitQueries.habitById(habitId)
        }
    ) {
        if (it != null) {
            Loaded(habit = it)
        }
    }
}

@Composable
private fun Loaded(habit: Habit) {
    val habitTrackCreationStrings = AppData.resources.strings.habitTrackCreationStrings
    val habitTrackQueries = AppData.database.habitTrackQueries
    val navigator = LocalNavigator.currentOrThrow
    val timeZone by AppData.dateTime.currentTimeZoneState.collectAsState()
    val currentTime by AppData.dateTime.currentTimeState.collectAsState()

    var rangeSelectionShow by rememberSaveable { mutableStateOf(false) }
    var selectedTimeSelectionIndex by remember { mutableIntStateOf(0) }

    var selectedDateTimeRange by remember {
        mutableStateOf(
            currentTime.toLocalDateTime(timeZone)
                .withTime(minute = 0, second = 0)
                .let { it..it }
        )
    }
    var timeRangeIncorrectReason by remember(selectedDateTimeRange) {
        mutableStateOf<HabitTrackTimeRangeIncorrectReason?>(null)
    }

    var eventCount by rememberSaveable { mutableIntStateOf(0) }
    var eventCountIncorrectReason by remember {
        mutableStateOf<HabitTrackEventCountIncorrectReason?>(null)
    }

    var comment by rememberSaveable {
        mutableStateOf("")
    }

    LaunchedEffect(selectedTimeSelectionIndex) {
        if (selectedTimeSelectionIndex == 0) {
            selectedDateTimeRange = currentTime.toLocalDateTime(timeZone)
                .withTime(minute = 0, second = 0)
                .let { it..it }
        }

        if (selectedTimeSelectionIndex == 1) {
            selectedDateTimeRange = currentTime.toLocalDateTime(timeZone)
                .yesterday()
                .withTime(minute = 0, second = 0)
                .let { it..it }
        }
    }

    if (rangeSelectionShow) {
        val state = rememberSelectionCalendarState(
            initialSelectedDateTime = selectedDateTimeRange,
            monthRange = dateSelectionMonthRange(timeZone)
        )

        RangeSelectionCalendarDialog(
            state = state,
            onCancel = {
                rangeSelectionShow = false
            },
            onConfirm = {
                rangeSelectionShow = false
                selectedTimeSelectionIndex = 2
                selectedDateTimeRange = it
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        SimpleTopAppBar(
            title = habitTrackCreationStrings.titleText(habit.name),
            onBackClick = navigator::pop
        )

        Spacer(Modifier.height(12.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = habitTrackCreationStrings.trackEventCountDescription()
        )

        Spacer(Modifier.height(12.dp))

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            value = eventCount.toString(),
            onValueChange = {
                eventCount = it.toIntOrNull() ?: 0
                eventCountIncorrectReason = null
            },
            label = habitTrackCreationStrings.trackEventCountLabel(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            regex = Regexps.integersOrEmpty(maxCharCount = 4),
            error = eventCountIncorrectReason?.let(habitTrackCreationStrings::trackEventCountError),
        )

        Spacer(Modifier.height(8.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = habitTrackCreationStrings.trackTimeDescription()
        )

        Spacer(Modifier.height(12.dp))

        SingleSelectionChipRow(
            items = listOf(
                habitTrackCreationStrings.now(),
                habitTrackCreationStrings.yesterday(),
                habitTrackCreationStrings.yourInterval()
            ),
            onClick = {
                if (it == 2) {
                    rangeSelectionShow = true
                }
                selectedTimeSelectionIndex = it
            },
            selectedIndex = selectedTimeSelectionIndex
        )

        Spacer(Modifier.height(12.dp))

        Button(
            modifier = Modifier.padding(horizontal = 16.dp),
            onClick = {
                rangeSelectionShow = true
            },
            text = selectedDateTimeRange.formatted()
        )

        Spacer(Modifier.height(12.dp))

        timeRangeIncorrectReason?.let {
            ErrorText(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = habitTrackCreationStrings.trackTimeRangeError(it)
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = habitTrackCreationStrings.commentDescription()
        )

        Spacer(Modifier.height(12.dp))

        TextField(
            modifier = Modifier.padding(horizontal = 16.dp),
            value = comment,
            onValueChange = {
                comment = it
            }
        )

        Spacer(modifier = Modifier.weight(1.0f))

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.End),
            text = habitTrackCreationStrings.finishDescription()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.End),
            text = habitTrackCreationStrings.finishButton(),
            type = Button.Type.Main,
            onClick = {
                eventCountIncorrectReason = eventCount.habitTrackEventCountIncorrectReason()
                if (eventCountIncorrectReason != null) return@Button

                timeRangeIncorrectReason = selectedDateTimeRange.habitTrackTimeRangeIncorrectReason(
                    currentTime = currentTime,
                    timeZone = timeZone
                )
                if (timeRangeIncorrectReason != null) return@Button

                val startTime = selectedDateTimeRange.start.toInstant(timeZone)
                val endTime = selectedDateTimeRange.endInclusive.toInstant(timeZone)

                habitTrackQueries.insert(
                    habitId = habit.id,
                    startTime = startTime,
                    endTime = endTime,
                    eventCount = totalHabitTrackEventCountByDaily(eventCount, startTime, endTime, timeZone),
                    comment = comment
                )
                navigator.pop()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}