package epicarchitect.breakbadhabits.ui.screen.habits.creation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import epicarchitect.breakbadhabits.operation.habits.totalHabitEventRecordEventCountByDaily
import epicarchitect.breakbadhabits.operation.habits.validation.HabitEventRecordDailyEventCountIncorrectReason
import epicarchitect.breakbadhabits.operation.habits.validation.HabitNewNameIncorrectReason
import epicarchitect.breakbadhabits.operation.habits.validation.habitEventRecordDailyEventCountIncorrectReason
import epicarchitect.breakbadhabits.operation.habits.validation.habitNewNameIncorrectReason
import epicarchitect.breakbadhabits.ui.component.Icon
import epicarchitect.breakbadhabits.ui.component.SimpleTopAppBar
import epicarchitect.breakbadhabits.ui.component.SingleSelectionChipRow
import epicarchitect.breakbadhabits.ui.component.SingleSelectionGrid
import epicarchitect.breakbadhabits.ui.component.button.Button
import epicarchitect.breakbadhabits.ui.component.regex.Regexps
import epicarchitect.breakbadhabits.ui.component.text.Text
import epicarchitect.breakbadhabits.ui.component.text.TextField
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
    val strings = AppData.resources.strings.habitCreationStrings
    val icons = AppData.resources.icons
    val habitQueries = AppData.database.habitQueries
    val navigator = LocalNavigator.currentOrThrow

    var habitName by rememberSaveable { mutableStateOf("") }
    var habitNameIncorrectReason by remember {
        mutableStateOf<HabitNewNameIncorrectReason?>(null)
    }

    var selectedIconId by rememberSaveable { mutableIntStateOf(0) }
    val selectedIcon = remember(selectedIconId) {
        icons.habitIcons.getById(selectedIconId)
    }

    var selectedHabitTimeIndex by rememberSaveable { mutableIntStateOf(0) }

    var dailyEventCount by rememberSaveable { mutableIntStateOf(0) }
    var dailyEventCountIncorrectReason by remember {
        mutableStateOf<HabitEventRecordDailyEventCountIncorrectReason?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        SimpleTopAppBar(
            title = strings.titleText(),
            onBackClick = navigator::pop
        )

        Spacer(Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = strings.habitNameDescription(),
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
                habitNameIncorrectReason = null
            },
            label = strings.habitNameLabel(),
            error = habitNameIncorrectReason?.let(strings::habitNameValidationError),
            description = strings.habitNameDescription()
        )

        Spacer(Modifier.height(24.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = strings.habitIconDescription(),
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
            text = strings.habitTimeDescription(),
            type = Text.Type.Description,
            priority = Text.Priority.Medium
        )

        Spacer(Modifier.height(12.dp))

        SingleSelectionChipRow(
            items = HabitCreationTime.entries.map(strings::habitTime),
            onClick = {
                selectedHabitTimeIndex = it
            },
            selectedIndex = selectedHabitTimeIndex,
            edgePadding = 16.dp
        )

        Spacer(Modifier.height(24.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = strings.trackEventCountDescription(),
            type = Text.Type.Description,
            priority = Text.Priority.Medium
        )

        Spacer(Modifier.height(12.dp))

        TextField(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            value = dailyEventCount.toString(),
            onValueChange = {
                dailyEventCount = it.toIntOrNull() ?: 0
                dailyEventCountIncorrectReason = null
            },
            error = dailyEventCountIncorrectReason?.let(strings::trackEventCountError),
            label = strings.trackEventCountLabel(),
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
                habitNameIncorrectReason = habitName.habitNewNameIncorrectReason(
                    maxLength = AppData.habitsConfig.maxHabitNameLength,
                    nameIsExists = { habitQueries.countWithName(it).executeAsOne() > 0L }
                )
                if (habitNameIncorrectReason != null) return@Button

                dailyEventCountIncorrectReason = dailyEventCount.habitEventRecordDailyEventCountIncorrectReason()
                if (dailyEventCountIncorrectReason != null) return@Button

                val selectedHabitTime = HabitCreationTime.entries[selectedHabitTimeIndex]
                val endTime = AppData.dateTime.currentTimeState.value
                val timeZone = AppData.dateTime.currentTimeZoneState.value
                val startTime = endTime - selectedHabitTime.offset + 1.days // fix offset

                habitQueries.insertWithEventRecord(
                    habitName = habitName,
                    habitIconId = selectedIconId,
                    trackEventCount = totalHabitEventRecordEventCountByDaily(
                        dailyEventCount = dailyEventCount,
                        startTime = startTime,
                        endTime = endTime,
                        timeZone = timeZone
                    ),
                    trackStartTime = startTime,
                    trackEndTime = endTime
                )
                navigator.pop()
            },
            text = strings.finishButtonText(),
            type = Button.Type.Main,
            icon = { Icon(icons.commonIcons.done) }
        )

        Spacer(Modifier.height(16.dp))
    }
}