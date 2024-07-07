package epicarchitect.breakbadhabits.ui.screen.habits.records.creation

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.data.AppData
import epicarchitect.breakbadhabits.data.Habit
import epicarchitect.breakbadhabits.operation.datetime.toInstantRange
import epicarchitect.breakbadhabits.operation.datetime.toLocalDateTimeRange
import epicarchitect.breakbadhabits.operation.datetime.yesterday
import epicarchitect.breakbadhabits.operation.habits.totalHabitEventCountByDaily
import epicarchitect.breakbadhabits.operation.habits.validation.DailyHabitEventCountError
import epicarchitect.breakbadhabits.operation.habits.validation.HabitEventRecordTimeRangeError
import epicarchitect.breakbadhabits.operation.habits.validation.checkDailyHabitEventCount
import epicarchitect.breakbadhabits.operation.habits.validation.checkHabitEventRecordTimeRange
import epicarchitect.breakbadhabits.ui.component.FlowStateContainer
import epicarchitect.breakbadhabits.ui.component.SimpleScrollableScreen
import epicarchitect.breakbadhabits.ui.component.SingleSelectionChipRow
import epicarchitect.breakbadhabits.ui.component.button.Button
import epicarchitect.breakbadhabits.ui.component.button.ButtonStyles
import epicarchitect.breakbadhabits.ui.component.calendar.RangeSelectionCalendarDialog
import epicarchitect.breakbadhabits.ui.component.calendar.rememberSelectionCalendarState
import epicarchitect.breakbadhabits.ui.component.regex.Regexps
import epicarchitect.breakbadhabits.ui.component.stateOfOneOrNull
import epicarchitect.breakbadhabits.ui.component.text.InputCard
import epicarchitect.breakbadhabits.ui.component.text.Text
import epicarchitect.breakbadhabits.ui.component.text.TextInputCard
import epicarchitect.breakbadhabits.ui.format.formatted
import epicarchitect.breakbadhabits.ui.screen.habits.records.editing.dateSelectionMonthRange

class HabitEventRecordCreationScreen(private val habitId: Int) : Screen {
    @Composable
    override fun Content() {
        HabitEventRecordCreation(habitId)
    }
}

@Composable
fun HabitEventRecordCreation(habitId: Int) {
    val strings = AppData.resources.strings.habitEventRecordCreationStrings
    val habitQueries = AppData.database.habitQueries
    val navigator = LocalNavigator.currentOrThrow

    FlowStateContainer(
        state = stateOfOneOrNull { habitQueries.habitById(habitId) }
    ) { habit ->
        SimpleScrollableScreen(
            title = strings.titleText(habit?.name ?: "..."),
            onBackClick = navigator::pop
        ) {
            if (habit != null) {
                Content(habit)
            }
        }
    }
}

@Composable
private fun ColumnScope.Content(habit: Habit) {
    val strings = AppData.resources.strings.habitEventRecordCreationStrings
    val habitEventRecordQueries = AppData.database.habitEventRecordQueries
    val navigator = LocalNavigator.currentOrThrow
    val timeZone by AppData.dateTime.currentTimeZoneState.collectAsState()
    val currentTime by AppData.dateTime.currentTimeState.collectAsState()

    var showRangeSelection by rememberSaveable { mutableStateOf(false) }
    var selectedTimeSelectionIndex by remember { mutableIntStateOf(0) }

    var selectedTimeRange by remember { mutableStateOf(currentTime.let { it..it }) }
    var timeRangeError by remember(selectedTimeRange) { mutableStateOf<HabitEventRecordTimeRangeError?>(null) }

    var dailyEventCount by rememberSaveable { mutableIntStateOf(0) }
    var dailyEventCountError by remember { mutableStateOf<DailyHabitEventCountError?>(null) }

    var comment by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(selectedTimeSelectionIndex) {
        if (selectedTimeSelectionIndex == 0) {
            selectedTimeRange = currentTime.let { it..it }
        }

        if (selectedTimeSelectionIndex == 1) {
            selectedTimeRange = currentTime.yesterday().let { it..it }
        }
    }

    if (showRangeSelection) {
        RangeSelectionCalendarDialog(
            state = rememberSelectionCalendarState(
                initialSelectedDateTime = selectedTimeRange.toLocalDateTimeRange(timeZone),
                monthRange = dateSelectionMonthRange(timeZone)
            ),
            onCancel = {
                showRangeSelection = false
            },
            onConfirm = {
                showRangeSelection = false
                selectedTimeSelectionIndex = 2
                selectedTimeRange = it.toInstantRange(timeZone)
            }
        )
    }

    Spacer(Modifier.height(16.dp))

    TextInputCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        title = strings.dailyEventCountTitle(),
        description = strings.dailyEventCountDescription(),
        value = dailyEventCount.toString(),
        onValueChange = {
            dailyEventCount = it.toIntOrNull() ?: 0
            dailyEventCountError = null
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        regex = Regexps.integersOrEmpty(maxCharCount = 4),
        error = dailyEventCountError?.let(strings::dailyEventCountError),
    )

    Spacer(Modifier.height(16.dp))

    InputCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        title = strings.timeRangeTitle(),
        description = strings.timeRangeDescription(),
        error = timeRangeError?.let(strings::timeRangeError)
    ) {
        Text(
            modifier = Modifier.padding(
                horizontal = it.calculateStartPadding(LocalLayoutDirection.current)
            ),
            text = selectedTimeRange.formatted(timeZone)
        )

        Spacer(modifier = Modifier.height(16.dp))

        SingleSelectionChipRow(
            items = listOf(
                strings.now(),
                strings.yesterday(),
                strings.yourTimeRange()
            ),
            onClick = { index ->
                if (index == 2) {
                    showRangeSelection = true
                }
                selectedTimeSelectionIndex = index
            },
            selectedIndex = selectedTimeSelectionIndex
        )
    }

    Spacer(Modifier.height(16.dp))

    TextInputCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        title = strings.commentTitle(),
        description = strings.commentDescription(),
        value = comment,
        onValueChange = {
            comment = it
        },
        multiline = true
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
            dailyEventCountError = checkDailyHabitEventCount(dailyEventCount)
            if (dailyEventCountError != null) return@Button

            timeRangeError = checkHabitEventRecordTimeRange(
                timeRange = selectedTimeRange,
                currentTime = currentTime
            )
            if (timeRangeError != null) return@Button

            habitEventRecordQueries.insert(
                habitId = habit.id,
                startTime = selectedTimeRange.start,
                endTime = selectedTimeRange.endInclusive,
                eventCount = totalHabitEventCountByDaily(
                    dailyEventCount = dailyEventCount,
                    timeRange = selectedTimeRange,
                    timeZone = timeZone
                ),
                comment = comment
            )
            navigator.pop()
        }
    )

    Spacer(modifier = Modifier.height(16.dp))
}