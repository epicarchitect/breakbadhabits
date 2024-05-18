package epicarchitect.breakbadhabits.ui.habits.editing

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.data.AppData
import epicarchitect.breakbadhabits.entity.util.flowOfOneOrNull
import epicarchitect.breakbadhabits.entity.validator.CorrectHabitNewName
import epicarchitect.breakbadhabits.entity.validator.HabitNewNameValidator
import epicarchitect.breakbadhabits.entity.validator.IncorrectHabitNewName
import epicarchitect.breakbadhabits.entity.validator.ValidatedHabitNewName
import epicarchitect.breakbadhabits.ui.dashboard.DashboardScreen
import epicarchitect.breakbadhabits.uikit.Dialog
import epicarchitect.breakbadhabits.uikit.Icon
import epicarchitect.breakbadhabits.uikit.SingleSelectionGrid
import epicarchitect.breakbadhabits.uikit.button.Button
import epicarchitect.breakbadhabits.uikit.effect.ClearFocusWhenKeyboardHiddenEffect
import epicarchitect.breakbadhabits.uikit.ext.onFocusLost
import epicarchitect.breakbadhabits.uikit.text.Text
import epicarchitect.breakbadhabits.uikit.text.TextField

class HabitEditingScreen(private val habitId: Int) : Screen {
    @Composable
    override fun Content() {
        HabitEditing(habitId)
    }
}

@Composable
fun HabitEditing(habitId: Int) {
    val navigator = LocalNavigator.currentOrThrow
    val habitQueries = AppData.database.habitQueries
    val resources by AppData.resources.collectAsState()
    val habitEditingStrings = resources.strings.habitEditingStrings
    val icons = resources.icons

    val initialHabit by remember(habitId) {
        habitQueries.habitById(habitId).flowOfOneOrNull()
    }.collectAsState(null)

    var habitName by rememberSaveable(initialHabit) { mutableStateOf(initialHabit?.name ?: "") }
    var validatedHabitName by remember { mutableStateOf<ValidatedHabitNewName?>(null) }
    var selectedIconId by rememberSaveable(initialHabit) { mutableIntStateOf(initialHabit?.iconId ?: 0) }

    ClearFocusWhenKeyboardHiddenEffect()

    var deletionShow by remember { mutableStateOf(false) }
    if (deletionShow) {
        Dialog(onDismiss = { deletionShow = false }) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = habitEditingStrings.deleteConfirmation(),
                    type = Text.Type.Description,
                    priority = Text.Priority.High
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Button(
                        text = habitEditingStrings.cancel(),
                        onClick = {
                            deletionShow = false
                        }
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        text = habitEditingStrings.yes(),
                        type = Button.Type.Main,
                        onClick = {
                            habitQueries.deleteById(habitId)
                            navigator.popUntil { it is DashboardScreen }
                        }
                    )
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = habitEditingStrings.titleText(),
            type = Text.Type.Title,
            priority = Text.Priority.High
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = habitEditingStrings.habitNameDescription()
        )

        Spacer(Modifier.height(16.dp))

        TextField(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .onFocusLost {
                    validatedHabitName = HabitNewNameValidator().validate(habitName)
                },
            value = habitName,
            onValueChange = {
                habitName = it.toString()
                validatedHabitName = HabitNewNameValidator().validate(habitName)
            },
            label = habitEditingStrings.habitNameLabel(),
            error = (validatedHabitName as? IncorrectHabitNewName)?.let {
                habitEditingStrings.habitNameValidationError(it.reason)
            },
            description = habitEditingStrings.habitNameDescription()
        )

        Spacer(Modifier.height(24.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = habitEditingStrings.habitIconDescription(),
            type = Text.Type.Description,
            priority = Text.Priority.Medium
        )

        Spacer(Modifier.height(12.dp))

        SingleSelectionGrid(
            modifier = Modifier.padding(horizontal = 16.dp),
            items = icons.habitIcons,
            selectedItem = icons.habitIcons.getById(selectedIconId),
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
            text = habitEditingStrings.deleteDescription()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            text = habitEditingStrings.deleteButton(),
            type = Button.Type.Dangerous,
            onClick = {
                deletionShow = true
            }
        )

        Spacer(modifier = Modifier.weight(1.0f))

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.End),
            onClick = {
                habitQueries.update(
                    id = habitId,
                    name = (validatedHabitName as CorrectHabitNewName).data,
                    iconId = selectedIconId
                )
                navigator.pop()
            },
            text = habitEditingStrings.finishButtonText(),
            type = Button.Type.Main,
            icon = { Icon(icons.commonIcons.Done) },
            enabled = validatedHabitName is CorrectHabitNewName
        )

        Spacer(Modifier.height(16.dp))
    }
}