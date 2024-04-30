package epicarchitect.breakbadhabits.screens.habits.creation

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
import epicarchitect.breakbadhabits.HabitIcons
import epicarchitect.breakbadhabits.HabitsConfigProvider
import epicarchitect.breakbadhabits.UpdatingAppTime
import epicarchitect.breakbadhabits.VectorIcons
import epicarchitect.breakbadhabits.database.AppData
import epicarchitect.breakbadhabits.foundation.icons.get
import epicarchitect.breakbadhabits.foundation.uikit.Icon
import epicarchitect.breakbadhabits.foundation.uikit.IconButton
import epicarchitect.breakbadhabits.foundation.uikit.SingleSelectionChipRow
import epicarchitect.breakbadhabits.foundation.uikit.SingleSelectionGrid
import epicarchitect.breakbadhabits.foundation.uikit.button.Button
import epicarchitect.breakbadhabits.foundation.uikit.effect.ClearFocusWhenKeyboardHiddenEffect
import epicarchitect.breakbadhabits.foundation.uikit.ext.onFocusLost
import epicarchitect.breakbadhabits.foundation.uikit.regex.Regexps
import epicarchitect.breakbadhabits.foundation.uikit.text.Text
import epicarchitect.breakbadhabits.foundation.uikit.text.TextField
import epicarchitect.breakbadhabits.newarch.habits.DefaultHabitCreationOperation
import epicarchitect.breakbadhabits.validator.CorrectHabitNewName
import epicarchitect.breakbadhabits.validator.CorrectHabitTrackEventCount
import epicarchitect.breakbadhabits.validator.HabitNewNameValidator
import epicarchitect.breakbadhabits.validator.HabitTrackEventCountValidator
import epicarchitect.breakbadhabits.validator.IncorrectHabitNewName
import epicarchitect.breakbadhabits.validator.IncorrectHabitTrackEventCount
import epicarchitect.breakbadhabits.validator.ValidatedHabitNewName
import epicarchitect.breakbadhabits.validator.ValidatedHabitTrackEventCount

class HabitCreationScreen : Screen {
    @Composable
    override fun Content() {
        HabitCreation()
    }
}

@Composable
fun HabitCreation() {
    val resources = LocalHabitCreationResources.current
    val navigator = LocalNavigator.currentOrThrow

    var habitName by rememberSaveable { mutableStateOf("") }
    var validatedHabitName by remember { mutableStateOf<ValidatedHabitNewName?>(null) }

    var selectedIconId by rememberSaveable { mutableIntStateOf(0) }
    val selectedIcon = remember(selectedIconId) { HabitIcons[selectedIconId] }

    var selectedHabitTimeIndex by rememberSaveable { mutableIntStateOf(0) }
    val selectedHabitTime = remember(selectedHabitTimeIndex) {
        DefaultHabitCreationOperation.HabitTime.entries[selectedHabitTimeIndex]
    }

    var trackEventCount by rememberSaveable { mutableIntStateOf(0) }
    var validatedTrackEventCount by remember { mutableStateOf<ValidatedHabitTrackEventCount?>(null) }

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
                Icon(VectorIcons.ArrowBack)
            }

            Spacer(Modifier.width(8.dp))

            Text(
                text = resources.titleText(),
                type = Text.Type.Title,
                priority = Text.Priority.High
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = resources.habitNameDescription(),
            type = Text.Type.Description,
            priority = Text.Priority.Medium
        )

        Spacer(Modifier.height(12.dp))

        TextField(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .onFocusLost {
                    validatedHabitName = HabitNewNameValidator(
                        mainDatabase = AppData.mainDatabase,
                        configProvider = HabitsConfigProvider()
                    ).validate(habitName)
                },
            value = habitName,
            onValueChange = {
                habitName = it
            },
            label = resources.habitNameLabel(),
            error = (validatedHabitName as? IncorrectHabitNewName)?.let {
                resources.habitNameValidationError(it.reason)
            },
            description = resources.habitNameDescription()
        )

        Spacer(Modifier.height(24.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = resources.habitIconDescription(),
            type = Text.Type.Description,
            priority = Text.Priority.Medium
        )

        Spacer(Modifier.height(12.dp))

        SingleSelectionGrid(
            modifier = Modifier.padding(horizontal = 16.dp),
            items = HabitIcons.list,
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
            items = DefaultHabitCreationOperation.HabitTime.entries.map(resources::habitTime),
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
                .onFocusLost {
                    validatedTrackEventCount = HabitTrackEventCountValidator().validate(trackEventCount)
                },
            value = trackEventCount.toString(),
            onValueChange = {
                trackEventCount = it.toIntOrNull() ?: 0
            },
            error = (validatedTrackEventCount as? IncorrectHabitTrackEventCount)?.let {
                resources.trackEventCountError(it.reason)
            },
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
                DefaultHabitCreationOperation(
                    idGenerator = AppData.idGenerator,
                    mainDatabase = AppData.mainDatabase,
                    name = validatedHabitName as CorrectHabitNewName,
                    icon = selectedIcon,
                    trackEventCount = validatedTrackEventCount as CorrectHabitTrackEventCount,
                    time = selectedHabitTime,
                    appTime = UpdatingAppTime.state().value
                ).execute()
                navigator.pop()
            },
            text = resources.finishButtonText(),
            type = Button.Type.Main,
            icon = { Icon(VectorIcons.Done) },
            enabled = validatedHabitName is CorrectHabitNewName &&
                trackEventCount != 0 &&
                validatedTrackEventCount is CorrectHabitTrackEventCount
        )

        Spacer(Modifier.height(16.dp))
    }
}