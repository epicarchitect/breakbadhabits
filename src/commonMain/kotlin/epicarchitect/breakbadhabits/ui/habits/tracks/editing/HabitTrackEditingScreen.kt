package epicarchitect.breakbadhabits.ui.habits.tracks.editing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.database.AppData
import epicarchitect.breakbadhabits.entity.datetime.UpdatingAppTime
import epicarchitect.breakbadhabits.entity.datetime.date
import epicarchitect.breakbadhabits.entity.datetime.time
import epicarchitect.breakbadhabits.entity.validator.HabitTrackEventCountValidator
import epicarchitect.breakbadhabits.entity.validator.IncorrectHabitTrackEventCount
import epicarchitect.breakbadhabits.entity.validator.ValidatedHabitTrackEventCount
import epicarchitect.breakbadhabits.uikit.Dialog
import epicarchitect.breakbadhabits.uikit.SingleSelectionChipRow
import epicarchitect.breakbadhabits.uikit.button.Button
import epicarchitect.breakbadhabits.uikit.effect.ClearFocusWhenKeyboardHiddenEffect
import epicarchitect.breakbadhabits.uikit.ext.onFocusLost
import epicarchitect.breakbadhabits.uikit.regex.Regexps
import epicarchitect.breakbadhabits.uikit.text.Text
import epicarchitect.breakbadhabits.uikit.text.TextField
import epicarchitect.calendar.compose.datepicker.EpicDatePicker
import epicarchitect.calendar.compose.datepicker.state.EpicDatePickerState
import epicarchitect.calendar.compose.datepicker.state.rememberEpicDatePickerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant

class HabitTrackEditingScreen(private val habitTrackId: Int) : Screen {
    @Composable
    override fun Content() {
        HabitTrackEditing(habitTrackId)
    }
}

@Composable
fun HabitTrackEditing(habitTrackId: Int) {
    val resources = LocalHabitTrackEditingResources.current
    val navigator = LocalNavigator.currentOrThrow

    val initialHabitTrack by remember(habitTrackId) {
        AppData.database.habitTrackQueries
            .selectById(habitTrackId)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
    }.collectAsState(null)

    val habit by remember(initialHabitTrack) {
        initialHabitTrack?.let {
            AppData.database.habitQueries
                .selectById(it.habitId)
                .asFlow()
                .mapToOneOrNull(Dispatchers.IO)
        } ?: emptyFlow()
    }.collectAsState(null)

    var rangeSelectionShow by rememberSaveable { mutableStateOf(false) }
    var selectedTimeSelectionIndex by remember { mutableIntStateOf(0) }

    var selectedDates by rememberSaveable {
        mutableStateOf(listOf(UpdatingAppTime.date()))
    }
    var selectedTimeInDates by rememberSaveable {
        mutableStateOf(listOf(UpdatingAppTime.time()))
    }

    var eventCount by rememberSaveable(initialHabitTrack) {
        mutableIntStateOf(initialHabitTrack?.eventCount ?: 0)
    }
    var validatedEventCount by remember {
        mutableStateOf<ValidatedHabitTrackEventCount?>(null)
    }
    var comment by rememberSaveable {
        mutableStateOf("")
    }

    LaunchedEffect(selectedTimeSelectionIndex) {
        if (selectedTimeSelectionIndex == 0) {
            selectedDates = listOf(UpdatingAppTime.date())
            selectedTimeInDates = listOf(UpdatingAppTime.time())
        }

        if (selectedTimeSelectionIndex == 1) {
            selectedDates = listOf(UpdatingAppTime.date().minus(DatePeriod(days = 1)))
            selectedTimeInDates = listOf(UpdatingAppTime.time())
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
            text = resources.titleText(),
            type = Text.Type.Title,
            priority = Text.Priority.High
        )

        Spacer(Modifier.height(4.dp))

        habit?.let {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = resources.habitNameLabel(it.name),
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
            modifier = Modifier.onFocusLost {
                validatedEventCount = HabitTrackEventCountValidator().validate(eventCount)
            },
            value = eventCount.toString(),
            onValueChange = {
                eventCount = it.toIntOrNull() ?: 0
            },
            label = "Число событий в день",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            regex = Regexps.integersOrEmpty(maxCharCount = 4),
            error = (validatedEventCount as? IncorrectHabitTrackEventCount)?.let {
                resources.trackEventCountError(it.reason)
            },
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
            text = resources.commentDescription()
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
            text = resources.finishDescription()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.End),
            text = resources.finishButton(),
            type = Button.Type.Main,
            onClick = {
                AppData.database.habitTrackQueries.update(
                    id = habitTrackId,
                    startTime = LocalDateTime(
                        date = selectedDates.first(),
                        time = selectedTimeInDates.first()
                    ).toInstant(UpdatingAppTime.timeZone()),
                    endTime = LocalDateTime(
                        date = selectedDates.last(),
                        time = selectedTimeInDates.last()
                    ).toInstant(UpdatingAppTime.timeZone()),
                    eventCount = eventCount,
                    comment = comment
                )
                navigator.pop()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}