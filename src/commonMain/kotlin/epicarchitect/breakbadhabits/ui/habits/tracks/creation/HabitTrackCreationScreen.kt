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
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDateRangePickerState
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
import epicarchitect.breakbadhabits.entity.datetime.PlatformDateTimeFormatter
import epicarchitect.breakbadhabits.entity.util.flowOfOneOrNull
import epicarchitect.breakbadhabits.entity.validator.HabitTrackEventCountInputValidation
import epicarchitect.breakbadhabits.uikit.Dialog
import epicarchitect.breakbadhabits.uikit.SimpleTopAppBar
import epicarchitect.breakbadhabits.uikit.SingleSelectionChipRow
import epicarchitect.breakbadhabits.uikit.button.Button
import epicarchitect.breakbadhabits.uikit.calendar.RangeSelectionCalendarDialog
import epicarchitect.breakbadhabits.uikit.calendar.rememberSelectionCalendarState
import epicarchitect.breakbadhabits.uikit.regex.Regexps
import epicarchitect.breakbadhabits.uikit.text.Text
import epicarchitect.breakbadhabits.uikit.text.TextField
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant

class HabitTrackCreationScreen(private val habitId: Int) : Screen {
    @Composable
    override fun Content() {
        HabitTrackCreation(habitId)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitTrackCreation(habitId: Int) {
    val habitTrackCreationStrings = AppData.resources.strings.habitTrackCreationStrings
    val habitQueries = AppData.database.habitQueries
    val habitTrackQueries = AppData.database.habitTrackQueries
    val navigator = LocalNavigator.currentOrThrow

    var rangeSelectionShow by rememberSaveable { mutableStateOf(false) }
    var selectedTimeSelectionIndex by remember { mutableIntStateOf(0) }

    val habit by remember(habitId) {
        habitQueries.habitById(habitId).flowOfOneOrNull()
    }.collectAsState(null)

    var selectedDates by rememberSaveable {
        mutableStateOf(listOf(AppData.userDateTime.local().date))
    }
    var selectedTimeInDates by rememberSaveable {
        mutableStateOf(listOf(AppData.userDateTime.local().time))
    }

    var eventCount by rememberSaveable { mutableIntStateOf(0) }
    var eventCountValidation by remember { mutableStateOf<HabitTrackEventCountInputValidation?>(null) }

    var comment by rememberSaveable {
        mutableStateOf("")
    }

    LaunchedEffect(selectedTimeSelectionIndex) {
        if (selectedTimeSelectionIndex == 0) {
            selectedDates = listOf(AppData.userDateTime.local().date)
            selectedTimeInDates = listOf(AppData.userDateTime.local().time)
        }

        if (selectedTimeSelectionIndex == 1) {
            selectedDates = listOf(AppData.userDateTime.local().date.minus(DatePeriod(days = 1)))
            selectedTimeInDates = listOf(AppData.userDateTime.local().time)
        }
    }

    if (rangeSelectionShow) {
        val state = rememberSelectionCalendarState(selectedDates)

        RangeSelectionCalendarDialog(
            state = state,
            onCancel = {
                rangeSelectionShow = false
            },
            onConfirm = {
                rangeSelectionShow = false
                selectedDates = state.epicState.selectedDates
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

        habit?.let {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = habitTrackCreationStrings.habitNameLabel(it.name),
                type = Text.Type.Description,
                priority = Text.Priority.Low
            )
        }

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
                eventCountValidation = null
            },
            label = "Число событий в день",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            regex = Regexps.integersOrEmpty(maxCharCount = 4),
            error = eventCountValidation?.incorrectReason()?.let(habitTrackCreationStrings::trackEventCountError),
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
            text = selectedDates.let {
                if (it.size == 1) {
                    val start = LocalDateTime(
                        date = it.first(),
                        time = selectedTimeInDates.first()
                    ).let(PlatformDateTimeFormatter::localDateTime)
                    "Дата и время: $start"
                } else if (it.size == 2) {
                    val start = LocalDateTime(
                        date = it.first(),
                        time = selectedTimeInDates.first()
                    ).let(PlatformDateTimeFormatter::localDateTime)
                    val end = LocalDateTime(
                        date = it.last(),
                        time = selectedTimeInDates.last()
                    ).let(PlatformDateTimeFormatter::localDateTime)
                    "Первое событие: $start, последнее событие: $end"
                } else {
                    "select"
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
                eventCountValidation = HabitTrackEventCountInputValidation(eventCount)
                if (eventCountValidation?.incorrectReason() != null) return@Button

                habitTrackQueries.insert(
                    habitId = habitId,
                    startTime = LocalDateTime(
                        date = selectedDates.first(),
                        time = selectedTimeInDates.first()
                    ).toInstant(AppData.userDateTime.timeZone()),
                    endTime = LocalDateTime(
                        date = selectedDates.last(),
                        time = selectedTimeInDates.last()
                    ).toInstant(AppData.userDateTime.timeZone()),
                    eventCount = eventCount,
                    comment = comment
                )
                navigator.pop()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}