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
import epicarchitect.breakbadhabits.entity.util.flowOfOneOrNull
import epicarchitect.breakbadhabits.entity.validator.ValidatedHabitTrackInput
import epicarchitect.breakbadhabits.uikit.Dialog
import epicarchitect.breakbadhabits.uikit.SingleSelectionChipRow
import epicarchitect.breakbadhabits.uikit.button.Button
import epicarchitect.breakbadhabits.uikit.effect.ClearFocusWhenKeyboardHiddenEffect
import epicarchitect.breakbadhabits.uikit.regex.Regexps
import epicarchitect.breakbadhabits.uikit.text.Text
import epicarchitect.breakbadhabits.uikit.text.TextField
import epicarchitect.calendar.compose.datepicker.EpicDatePicker
import epicarchitect.calendar.compose.datepicker.state.EpicDatePickerState
import epicarchitect.calendar.compose.datepicker.state.rememberEpicDatePickerState
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

@Composable
fun HabitTrackCreation(habitId: Int) {
    val resources by AppData.resources.collectAsState()
    val habitTrackCreationStrings = resources.strings.habitTrackCreationStrings
    val icons = resources.icons
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
    var validatedEventCount by remember { mutableStateOf(ValidatedHabitTrackInput(0)) }

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

    ClearFocusWhenKeyboardHiddenEffect()

    if (rangeSelectionShow) {
        val state = rememberEpicDatePickerState(
            selectedDates = selectedDates,
            selectionMode = EpicDatePickerState.SelectionMode.Range
        )
        Dialog(
            onDismiss = {
                rangeSelectionShow = false
                selectedDates = state.selectedDates
            }
        ) {
            EpicDatePicker(
                modifier = Modifier,
                state = state
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = habitTrackCreationStrings.titleText(),
            type = Text.Type.Title,
            priority = Text.Priority.High
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
            modifier = Modifier.fillMaxWidth(),
            value = eventCount.toString(),
            onValueChange = {
                val validated = ValidatedHabitTrackInput(it)
                eventCount = validated.toInt() ?: 0
                validatedEventCount = validated
            },
            label = "Число событий в день",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            regex = Regexps.integersOrEmpty(maxCharCount = 4),
            error = validatedEventCount.incorrectReason()?.let(habitTrackCreationStrings::trackEventCountError),
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
                    )
                    "Дата и время: $start"
                } else if (it.size == 2) {
                    val start = LocalDateTime(
                        date = it.first(),
                        time = selectedTimeInDates.first()
                    )
                    val end = LocalDateTime(
                        date = it.last(),
                        time = selectedTimeInDates.last()
                    )
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
                comment = it.toString()
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

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.End),
            text = habitTrackCreationStrings.finishButton(),
            type = Button.Type.Main,
            onClick = {
                AppData.database.habitTrackQueries.insert(
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