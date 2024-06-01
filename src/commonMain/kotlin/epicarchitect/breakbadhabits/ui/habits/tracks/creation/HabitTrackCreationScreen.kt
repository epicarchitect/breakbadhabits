package epicarchitect.breakbadhabits.ui.habits.tracks.creation

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
import epicarchitect.breakbadhabits.operation.datetime.formatted
import epicarchitect.breakbadhabits.operation.habits.HabitTrackEventCountIncorrectReason
import epicarchitect.breakbadhabits.operation.habits.eventCountByDaily
import epicarchitect.breakbadhabits.operation.habits.habitTrackEventCountIncorrectReason
import epicarchitect.breakbadhabits.uikit.FlowStateContainer
import epicarchitect.breakbadhabits.uikit.SimpleTopAppBar
import epicarchitect.breakbadhabits.uikit.SingleSelectionChipRow
import epicarchitect.breakbadhabits.uikit.button.Button
import epicarchitect.breakbadhabits.uikit.calendar.RangeSelectionCalendarDialog
import epicarchitect.breakbadhabits.uikit.calendar.RangeSelectionCalendarDialogResources
import epicarchitect.breakbadhabits.uikit.calendar.rememberSelectionCalendarState
import epicarchitect.breakbadhabits.uikit.regex.Regexps
import epicarchitect.breakbadhabits.uikit.stateOfOneOrNull
import epicarchitect.breakbadhabits.uikit.text.Text
import epicarchitect.breakbadhabits.uikit.text.TextField
import epicarchitect.calendar.compose.basis.EpicMonth
import epicarchitect.calendar.compose.basis.addYears
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.minus
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
    val currentTime by AppData.dateTime.currentInstantState.collectAsState()

    var rangeSelectionShow by rememberSaveable { mutableStateOf(false) }
    var selectedTimeSelectionIndex by remember { mutableIntStateOf(0) }

    var selectedDateTimeRange by remember {
        mutableStateOf(
            currentTime.toLocalDateTime(timeZone).let {
                LocalDateTime(it.date, LocalTime(it.hour, 0, 0))
            }.let { it..it }
        )
    }

    var eventCount by rememberSaveable { mutableIntStateOf(0) }
    var eventCountIncorrectReason by remember { mutableStateOf<HabitTrackEventCountIncorrectReason?>(null) }

    var comment by rememberSaveable {
        mutableStateOf("")
    }

    LaunchedEffect(selectedTimeSelectionIndex) {
        if (selectedTimeSelectionIndex == 0) {
            selectedDateTimeRange = currentTime.toLocalDateTime(timeZone).let {
                LocalDateTime(it.date, LocalTime(it.hour, 0, 0))
            }.let { it..it }
        }

        if (selectedTimeSelectionIndex == 1) {
            selectedDateTimeRange = currentTime.toLocalDateTime(timeZone).let {
                LocalDateTime(it.date.minus(DatePeriod(days = 1)), LocalTime(it.hour, 0, 0))
            }.let { it..it }
        }
    }

    if (rangeSelectionShow) {
        val state = rememberSelectionCalendarState(
            initialSelectedDateTime = selectedDateTimeRange,
            monthRange = EpicMonth.now(timeZone).let {
                it.addYears(-10).copy(month = Month.JANUARY)..it.copy(month = Month.DECEMBER)
            }
        )

        RangeSelectionCalendarDialog(
            state = state,
            resources = RangeSelectionCalendarDialogResources(),
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
            title = habitTrackCreationStrings.titleText(),
            onBackClick = navigator::pop
        )

        Spacer(Modifier.height(4.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = habitTrackCreationStrings.habitNameLabel(habit.name),
            type = Text.Type.Description,
            priority = Text.Priority.Low
        )

        Spacer(Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Укажите сколько примерно было событий привычки каждый день"
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
            label = "Число событий в день",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            regex = Regexps.integersOrEmpty(maxCharCount = 4),
            error = eventCountIncorrectReason?.let(habitTrackCreationStrings::trackEventCountError),
        )

        Spacer(Modifier.height(24.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Укажите когда произошло событие:"
        )

        Spacer(Modifier.height(12.dp))

        SingleSelectionChipRow(
            items = listOf("Сейчас", "Вчера", "Свой интервал"),
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
            text = selectedDateTimeRange.let {
                selectedDateTimeRange.let {
                    val start = it.start.formatted()
                    val end = it.endInclusive.formatted()
                    "Первое событие: $start, последнее событие: $end"
                }
            }
        )

        Spacer(Modifier.height(24.dp))

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
                eventCountIncorrectReason = habitTrackEventCountIncorrectReason(eventCount)
                if (eventCountIncorrectReason != null) return@Button

                val startTime = selectedDateTimeRange.start.toInstant(timeZone)
                val endTime = selectedDateTimeRange.endInclusive.toInstant(timeZone)

                habitTrackQueries.insert(
                    habitId = habit.id,
                    startTime = startTime,
                    endTime = endTime,
                    eventCount = eventCountByDaily(eventCount, startTime, endTime, timeZone),
                    comment = comment
                )
                navigator.pop()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}