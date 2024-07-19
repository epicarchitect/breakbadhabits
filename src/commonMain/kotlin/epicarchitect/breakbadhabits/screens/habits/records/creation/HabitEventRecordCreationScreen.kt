package epicarchitect.breakbadhabits.screens.habits.records.creation

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
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
import epicarchitect.breakbadhabits.Environment
import epicarchitect.breakbadhabits.uikit.DateTimeRangeInputCard
import epicarchitect.breakbadhabits.uikit.FlowStateContainer
import epicarchitect.breakbadhabits.uikit.SimpleScrollableScreen
import epicarchitect.breakbadhabits.uikit.button.Button
import epicarchitect.breakbadhabits.uikit.button.ButtonStyles
import epicarchitect.breakbadhabits.uikit.regex.Regexps
import epicarchitect.breakbadhabits.uikit.stateOfOneOrNull
import epicarchitect.breakbadhabits.uikit.text.TextInputCard
import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.habits.totalHabitEventCountByDaily
import epicarchitect.breakbadhabits.habits.validation.DailyHabitEventCountError
import epicarchitect.breakbadhabits.habits.validation.HabitEventRecordTimeRangeError
import epicarchitect.breakbadhabits.habits.validation.checkDailyHabitEventCount
import epicarchitect.breakbadhabits.habits.validation.checkHabitEventRecordTimeRange
import kotlin.time.Duration.Companion.hours

class HabitEventRecordCreationScreen(private val habitId: Int) : Screen {
    @Composable
    override fun Content() {
        HabitEventRecordCreation(habitId)
    }
}

@Composable
fun HabitEventRecordCreation(habitId: Int) {
    val strings = Environment.resources.strings.habitEventRecordEditingStrings
    val habitQueries = Environment.database.habitQueries
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
    val strings = Environment.resources.strings.habitEventRecordCreationStrings
    val habitEventRecordQueries = Environment.database.habitEventRecordQueries
    val navigator = LocalNavigator.currentOrThrow

    var selectedTimeRange by remember {
        val currentInstant = Environment.dateTime.currentInstant()
        mutableStateOf((currentInstant - 1.hours)..currentInstant)
    }

    var timeRangeError by remember(selectedTimeRange) {
        mutableStateOf<HabitEventRecordTimeRangeError?>(null)
    }

    var dailyEventCount by rememberSaveable { mutableIntStateOf(0) }
    var dailyEventCountError by remember { mutableStateOf<DailyHabitEventCountError?>(null) }

    var comment by rememberSaveable { mutableStateOf("") }

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


    DateTimeRangeInputCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        title = strings.timeRangeTitle(),
        description = strings.timeRangeDescription(),
        error = timeRangeError?.let(strings::timeRangeError),
        value = selectedTimeRange,
        onChanged = {
            selectedTimeRange = it
        },
        startTimeLabel = strings.startDateTimeLabel(),
        endTimeLabel = strings.endDateTimeLabel(),
        timeZone = Environment.dateTime.currentTimeZone()
    )

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
                currentTime = Environment.dateTime.currentInstant()
            )
            if (timeRangeError != null) return@Button

            habitEventRecordQueries.insert(
                habitId = habit.id,
                startTime = selectedTimeRange.start,
                endTime = selectedTimeRange.endInclusive,
                eventCount = totalHabitEventCountByDaily(
                    dailyEventCount = dailyEventCount,
                    timeRange = selectedTimeRange,
                    timeZone = Environment.dateTime.currentTimeZone()
                ),
                comment = comment
            )
            navigator.pop()
        }
    )

    Spacer(modifier = Modifier.height(16.dp))
}