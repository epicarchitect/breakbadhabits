package epicarchitect.breakbadhabits.ui.habits.creation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import epicarchitect.breakbadhabits.data.AppData
import epicarchitect.breakbadhabits.entity.validator.HabitNewNameValidation
import epicarchitect.breakbadhabits.entity.validator.HabitTrackEventCountInputValidation
import epicarchitect.breakbadhabits.uikit.Icon
import epicarchitect.breakbadhabits.uikit.IconButton
import epicarchitect.breakbadhabits.uikit.SingleSelectionChipRow
import epicarchitect.breakbadhabits.uikit.SingleSelectionGrid
import epicarchitect.breakbadhabits.uikit.button.Button
import epicarchitect.breakbadhabits.uikit.effect.ClearFocusWhenKeyboardHiddenEffect
import epicarchitect.breakbadhabits.uikit.regex.Regexps
import epicarchitect.breakbadhabits.uikit.text.Text
import epicarchitect.breakbadhabits.uikit.text.TextField
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

enum class HabitCreationTime(val offset: Duration) {
    MONTH_1(30.days),
    MONTH_3(90.days),
    MONTH_6(180.days),
    YEAR_1(365.days),
    YEAR_2(365.days * 2),
    YEAR_3(365.days * 3),
    YEAR_4(365.days * 4),
    YEAR_5(365.days * 5),
    YEAR_6(365.days * 6),
    YEAR_7(365.days * 7),
    YEAR_8(365.days * 8),
    YEAR_9(365.days * 9),
    YEAR_10(365.days * 10)
}

class HabitCreationScreen : Screen {
    @Composable
    override fun Content() {
        HabitCreation()
    }
}

@Composable
fun HabitCreation() {
    val habitCreationStrings = AppData.resources.strings.habitCreationStrings
    val icons = AppData.resources.icons
    val habitQueries = AppData.database.habitQueries
    val navigator = LocalNavigator.currentOrThrow

    var habitName by rememberSaveable { mutableStateOf("") }
    var habitNameValidation by remember { mutableStateOf<HabitNewNameValidation?>(null) }

    var selectedIconId by rememberSaveable { mutableIntStateOf(0) }
    val selectedIcon = remember(selectedIconId) { icons.habitIcons.getById(selectedIconId) }

    var selectedHabitTimeIndex by rememberSaveable { mutableIntStateOf(0) }
    val selectedHabitTime = remember(selectedHabitTimeIndex) {
        HabitCreationTime.entries[selectedHabitTimeIndex]
    }

    var trackEventCount by rememberSaveable { mutableIntStateOf(0) }
    var trackEventCountValidation by remember { mutableStateOf<HabitTrackEventCountInputValidation?>(null) }

    val inputIsValid = remember(habitNameValidation, trackEventCountValidation) {
        habitNameValidation?.let { name ->
            trackEventCountValidation?.let { eventCount ->
                name.incorrectReason() == null && eventCount.incorrectReason() == null
            } ?: false
        } ?: false
    }

    ClearFocusWhenKeyboardHiddenEffect()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = navigator::pop) {
                Icon(icons.commonIcons.arrowBack)
            }

            Spacer(Modifier.width(8.dp))

            Text(
                text = habitCreationStrings.titleText(),
                type = Text.Type.Title,
                priority = Text.Priority.High
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = habitCreationStrings.habitNameDescription(),
            type = Text.Type.Description,
            priority = Text.Priority.Medium
        )

        Spacer(Modifier.height(12.dp))

        TextField(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            value = habitName,
            onValueChange = {
                habitName = it
                habitNameValidation = null
            },
            label = habitCreationStrings.habitNameLabel(),
            error = habitNameValidation?.incorrectReason()?.let(habitCreationStrings::habitNameValidationError),
            description = habitCreationStrings.habitNameDescription()
        )

        Spacer(Modifier.height(24.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = habitCreationStrings.habitIconDescription(),
            type = Text.Type.Description,
            priority = Text.Priority.Medium
        )

        Spacer(Modifier.height(12.dp))

        SingleSelectionGrid(
            modifier = Modifier.padding(horizontal = 16.dp),
            items = icons.habitIcons,
            selectedItem = selectedIcon,
            cell = { icon ->
                Icon(
                    modifier = Modifier.size(24.dp),
                    icon = icon
                )
            },
            onSelect = {
                selectedIconId = it.id
            }
        )

        Spacer(Modifier.height(24.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Укажите примерно как давно у вас эта привычка:",
            type = Text.Type.Description,
            priority = Text.Priority.Medium
        )

        Spacer(Modifier.height(12.dp))

        SingleSelectionChipRow(
            items = HabitCreationTime.entries.map(habitCreationStrings::habitTime),
            onClick = {
                selectedHabitTimeIndex = it
            },
            selectedIndex = selectedHabitTimeIndex,
            edgePadding = 16.dp
        )

        Spacer(Modifier.height(24.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Укажите сколько примерно было событий привычки каждый день:",
            type = Text.Type.Description,
            priority = Text.Priority.Medium
        )

        Spacer(Modifier.height(12.dp))

        TextField(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            value = trackEventCount.toString(),
            onValueChange = {
                trackEventCount = it.toIntOrNull() ?: 0
                trackEventCountValidation = null
            },
            error = trackEventCountValidation?.incorrectReason()?.let(habitCreationStrings::trackEventCountError),
            label = "Число событий в день",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            regex = Regexps.integersOrEmpty(maxCharCount = 4)
        )

        Spacer(modifier = Modifier.weight(1.0f))

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.End),
            onClick = {
                habitNameValidation = HabitNewNameValidation(habitName)
                if (habitNameValidation?.incorrectReason() != null) return@Button

                trackEventCountValidation = HabitTrackEventCountInputValidation(trackEventCount)
                if (trackEventCountValidation?.incorrectReason() != null) return@Button

                val endTime = AppData.userDateTime.instant()
                val eventCountInDay = trackEventCount
                val startTime = endTime - selectedHabitTime.offset
                val duration = endTime - startTime
                val allEventCount = duration.inWholeDays.toInt() * eventCountInDay
                habitQueries.insertWithTrack(
                    habitName = habitName,
                    habitIconId = selectedIconId,
                    trackEventCount = allEventCount,
                    trackStartTime = startTime,
                    trackEndTime = endTime
                )
                navigator.pop()
            },
            text = habitCreationStrings.finishButtonText(),
            type = Button.Type.Main,
            icon = { Icon(icons.commonIcons.done) }
        )

        Spacer(Modifier.height(16.dp))
    }
}