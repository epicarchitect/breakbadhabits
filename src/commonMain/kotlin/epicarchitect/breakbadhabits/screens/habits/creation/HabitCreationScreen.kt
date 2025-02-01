package epicarchitect.breakbadhabits.screens.habits.creation

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
import epicarchitect.breakbadhabits.habits.totalHabitEventCountByDaily
import epicarchitect.breakbadhabits.habits.validation.DailyHabitEventCountError
import epicarchitect.breakbadhabits.habits.validation.HabitNewNameError
import epicarchitect.breakbadhabits.habits.validation.checkDailyHabitEventCount
import epicarchitect.breakbadhabits.habits.validation.checkHabitNewName
import epicarchitect.breakbadhabits.uikit.SimpleScrollableScreen
import epicarchitect.breakbadhabits.uikit.SingleSelectionChipRow
import epicarchitect.breakbadhabits.uikit.button.Button
import epicarchitect.breakbadhabits.uikit.button.ButtonStyles
import epicarchitect.breakbadhabits.uikit.regex.Regexps
import epicarchitect.breakbadhabits.uikit.text.InputCard
import epicarchitect.breakbadhabits.uikit.text.TextInputCard
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

enum class HabitDuration(val duration: Duration) {
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
    val strings = Environment.resources.strings.habitCreationStrings
    val navigator = LocalNavigator.currentOrThrow

    SimpleScrollableScreen(
        title = strings.titleText(),
        onBackClick = navigator::pop
    ) {
        Content()
    }
}

@Composable
private fun ColumnScope.Content() {
    val strings = Environment.resources.strings.habitCreationStrings
    val icons = Environment.resources.icons
    val habitQueries = Environment.database.habitQueries
    val navigator = LocalNavigator.currentOrThrow

    var habitName by rememberSaveable { mutableStateOf("") }
    var habitNameError by remember { mutableStateOf<HabitNewNameError?>(null) }

    var selectedDurationIndex by rememberSaveable { mutableIntStateOf(0) }
    val selectedHabitDuration = remember(selectedDurationIndex) {
        HabitDuration.entries[selectedDurationIndex].duration + 1.days
    }

    var dailyEventCount by rememberSaveable { mutableIntStateOf(0) }
    var dailyEventCountError by remember { mutableStateOf<DailyHabitEventCountError?>(null) }

    Spacer(Modifier.height(16.dp))

    TextInputCard(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        title = strings.habitNameTitle(),
        value = habitName,
        onValueChange = {
            habitName = it
            habitNameError = null
        },
        error = habitNameError?.let(strings::habitNameError),
        description = strings.habitNameDescription()
    )

    Spacer(Modifier.height(16.dp))

    InputCard(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        title = strings.habitDurationTitle(),
        description = strings.habitDurationDescription()
    ) {
        SingleSelectionChipRow(
            items = HabitDuration.entries.map(strings::habitDuration),
            onClick = { selectedDurationIndex = it },
            selectedIndex = selectedDurationIndex
        )
    }

    Spacer(Modifier.height(16.dp))

    TextInputCard(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        title = strings.habitEventCountTitle(),
        description = strings.trackEventCountDescription(),
        error = dailyEventCountError?.let(strings::trackEventCountError),
        value = dailyEventCount.toString(),
        onValueChange = {
            dailyEventCount = it.toIntOrNull() ?: 0
            dailyEventCountError = null
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        regex = Regexps.integersOrEmpty(maxCharCount = 4)
    )

    Spacer(modifier = Modifier.weight(1.0f))

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .align(Alignment.End),
        onClick = {
            habitNameError = checkHabitNewName(
                newName = habitName,
                maxLength = Environment.habitsRules.maxHabitNameLength,
                nameIsExists = { habitQueries.countWithName(it).executeAsOne() > 0L }
            )
            if (habitNameError != null) return@Button

            dailyEventCountError = checkDailyHabitEventCount(dailyEventCount)
            if (dailyEventCountError != null) return@Button

            val endTime = Environment.dateTime.currentInstant()
            val startTime = endTime - selectedHabitDuration

            habitQueries.insertWithEventRecord(
                habitName = habitName,
                trackEventCount = totalHabitEventCountByDaily(
                    dailyEventCount = dailyEventCount,
                    timeRange = startTime..endTime,
                    timeZone = Environment.dateTime.currentTimeZone()
                ),
                trackStartTime = startTime,
                trackEndTime = endTime
            )
            navigator.pop()
        },
        text = strings.finishButtonText(),
        style = ButtonStyles.primary,
        icon = icons.commonIcons.done
    )

    Spacer(modifier = Modifier.height(16.dp))
}