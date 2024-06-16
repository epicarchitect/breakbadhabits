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
import epicarchitect.breakbadhabits.operation.datetime.yesterday
import epicarchitect.breakbadhabits.operation.habits.totalHabitEventRecordEventCountByDaily
import epicarchitect.breakbadhabits.operation.habits.validation.HabitEventRecordDailyEventCountIncorrectReason
import epicarchitect.breakbadhabits.operation.habits.validation.HabitEventRecordTimeRangeIncorrectReason
import epicarchitect.breakbadhabits.operation.habits.validation.habitEventRecordDailyEventCountIncorrectReason
import epicarchitect.breakbadhabits.operation.habits.validation.habitEventRecordTimeRangeIncorrectReason
import epicarchitect.breakbadhabits.ui.component.FlowStateContainer
import epicarchitect.breakbadhabits.ui.component.SimpleScrollableScreen
import epicarchitect.breakbadhabits.ui.component.SingleSelectionChipRow
import epicarchitect.breakbadhabits.ui.component.button.Button
import epicarchitect.breakbadhabits.ui.component.calendar.RangeSelectionCalendarDialog
import epicarchitect.breakbadhabits.ui.component.calendar.rememberSelectionCalendarState
import epicarchitect.breakbadhabits.ui.component.regex.Regexps
import epicarchitect.breakbadhabits.ui.component.stateOfOneOrNull
import epicarchitect.breakbadhabits.ui.component.text.InputCard
import epicarchitect.breakbadhabits.ui.component.text.Text
import epicarchitect.breakbadhabits.ui.component.text.TextInputCard
import epicarchitect.breakbadhabits.ui.format.formatted
import epicarchitect.breakbadhabits.ui.screen.habits.records.editing.dateSelectionMonthRange
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

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

    var rangeSelectionShow by rememberSaveable { mutableStateOf(false) }
    var selectedTimeSelectionIndex by remember { mutableIntStateOf(0) }

    var selectedDateTimeRange by remember {
        mutableStateOf(currentTime.toLocalDateTime(timeZone).let { it..it })
    }
    var timeRangeIncorrectReason by remember(selectedDateTimeRange) {
        mutableStateOf<HabitEventRecordTimeRangeIncorrectReason?>(null)
    }

    var dailyEventCount by rememberSaveable { mutableIntStateOf(0) }
    var dailyEventCountIncorrectReason by remember {
        mutableStateOf<HabitEventRecordDailyEventCountIncorrectReason?>(null)
    }

    var comment by rememberSaveable {
        mutableStateOf("")
    }

    LaunchedEffect(selectedTimeSelectionIndex) {
        if (selectedTimeSelectionIndex == 0) {
            selectedDateTimeRange = currentTime.toLocalDateTime(timeZone).let { it..it }
        }

        if (selectedTimeSelectionIndex == 1) {
            selectedDateTimeRange = currentTime.toLocalDateTime(timeZone).yesterday().let { it..it }
        }
    }

    if (rangeSelectionShow) {
        RangeSelectionCalendarDialog(
            state = rememberSelectionCalendarState(
                initialSelectedDateTime = selectedDateTimeRange,
                monthRange = dateSelectionMonthRange(timeZone)
            ),
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
            dailyEventCountIncorrectReason = null
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        regex = Regexps.integersOrEmpty(maxCharCount = 4),
        error = dailyEventCountIncorrectReason?.let(strings::dailyEventCountError),
    )

    Spacer(Modifier.height(16.dp))

    InputCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        title = strings.timeRangeTitle(),
        description = strings.timeRangeDescription(),
        error = timeRangeIncorrectReason?.let(strings::timeRangeError)
    ) {
        Text(
            modifier = Modifier.padding(
                horizontal = it.calculateStartPadding(LocalLayoutDirection.current)
            ),
            text = selectedDateTimeRange.formatted()
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
                    rangeSelectionShow = true
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
        type = Button.Type.Main,
        onClick = {
            dailyEventCountIncorrectReason = dailyEventCount.habitEventRecordDailyEventCountIncorrectReason()
            if (dailyEventCountIncorrectReason != null) return@Button

            timeRangeIncorrectReason = selectedDateTimeRange.habitEventRecordTimeRangeIncorrectReason(
                currentTime = currentTime,
                timeZone = timeZone
            )
            if (timeRangeIncorrectReason != null) return@Button

            val startTime = selectedDateTimeRange.start.toInstant(timeZone)
            val endTime = selectedDateTimeRange.endInclusive.toInstant(timeZone)

            habitEventRecordQueries.insert(
                habitId = habit.id,
                startTime = startTime,
                endTime = endTime,
                eventCount = totalHabitEventRecordEventCountByDaily(dailyEventCount, startTime, endTime, timeZone),
                comment = comment
            )
            navigator.pop()
        }
    )

    Spacer(modifier = Modifier.height(16.dp))
}