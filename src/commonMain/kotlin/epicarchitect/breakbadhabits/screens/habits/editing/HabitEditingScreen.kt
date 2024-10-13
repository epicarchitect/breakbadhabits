package epicarchitect.breakbadhabits.screens.habits.editing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
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
import epicarchitect.breakbadhabits.Environment
import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.habits.validation.HabitNewNameError
import epicarchitect.breakbadhabits.habits.validation.checkHabitNewName
import epicarchitect.breakbadhabits.screens.appDashboard.AppDashboardScreen
import epicarchitect.breakbadhabits.uikit.Dialog
import epicarchitect.breakbadhabits.uikit.FlowStateContainer
import epicarchitect.breakbadhabits.uikit.Icon
import epicarchitect.breakbadhabits.uikit.SimpleScrollableScreen
import epicarchitect.breakbadhabits.uikit.SingleSelectionGrid
import epicarchitect.breakbadhabits.uikit.button.Button
import epicarchitect.breakbadhabits.uikit.button.ButtonStyles
import epicarchitect.breakbadhabits.uikit.stateOfOneOrNull
import epicarchitect.breakbadhabits.uikit.text.InputCard
import epicarchitect.breakbadhabits.uikit.text.Text
import epicarchitect.breakbadhabits.uikit.text.TextInputCard

class HabitEditingScreen(private val habitId: Int) : Screen {
    @Composable
    override fun Content() {
        HabitEditing(habitId)
    }
}

@Composable
fun HabitEditing(habitId: Int) {
    val navigator = LocalNavigator.currentOrThrow
    val habitQueries = Environment.database.habitQueries
    val strings = Environment.resources.strings.habitEditingStrings

    FlowStateContainer(
        state = stateOfOneOrNull { habitQueries.habitById(habitId) }
    ) { habit ->
        SimpleScrollableScreen(
            title = strings.titleText(),
            onBackClick = navigator::pop
        ) {
            if (habit != null) {
                Content(habit)
            }
        }
    }
}


@Composable
private fun ColumnScope.Content(initialHabit: Habit) {
    val navigator = LocalNavigator.currentOrThrow
    val habitQueries = Environment.database.habitQueries
    val strings = Environment.resources.strings.habitEditingStrings
    val icons = Environment.resources.icons

    var habitName by rememberSaveable(initialHabit) { mutableStateOf(initialHabit.name) }
    var habitNameError by remember { mutableStateOf<HabitNewNameError?>(null) }
    var selectedIconId by rememberSaveable(initialHabit) { mutableIntStateOf(initialHabit.iconId) }
    val selectedIcon = remember(selectedIconId) { icons.habitIcons.getById(selectedIconId) }

    var showDeletion by remember { mutableStateOf(false) }
    if (showDeletion) {
        DeletionDialog(
            habit = initialHabit,
            onDismiss = { showDeletion = false }
        )
    }

    Spacer(Modifier.height(16.dp))

    TextInputCard(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        value = habitName,
        onValueChange = {
            habitName = it
            habitNameError = null
        },
        title = strings.habitNameTitle(),
        description = strings.habitNameDescription(),
        error = habitNameError?.let(strings::habitNameError)
    )

    Spacer(Modifier.height(16.dp))

    InputCard(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        title = strings.habitIconTitle(),
        description = strings.habitIconDescription()
    ) {
        SingleSelectionGrid(
            modifier = Modifier.padding(it),
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
    }

    Spacer(Modifier.height(16.dp))

    Button(
        modifier = Modifier.padding(horizontal = 16.dp),
        text = strings.deleteButton(),
        style = ButtonStyles.dangerous,
        onClick = {
            showDeletion = true
        }
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
                initialName = initialHabit.name,
                maxLength = Environment.habitsRules.maxHabitNameLength,
                nameIsExists = {
                    Environment.database.habitQueries.countWithName(it).executeAsOne() > 0L
                }
            )
            if (habitNameError != null) return@Button

            habitQueries.update(
                id = initialHabit.id,
                name = habitName,
                iconId = selectedIconId
            )
            navigator.pop()
        },
        text = strings.finishButtonText(),
        style = ButtonStyles.primary,
        icon = icons.commonIcons.done
    )

    Spacer(Modifier.height(16.dp))
}

@Composable
private fun DeletionDialog(
    habit: Habit,
    onDismiss: () -> Unit
) {
    val navigator = LocalNavigator.currentOrThrow
    val habitQueries = Environment.database.habitQueries
    val strings = Environment.resources.strings.habitEditingStrings

    Dialog(onDismiss) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = strings.deleteConfirmation(),
                type = Text.Type.Description,
                priority = Text.Priority.High
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.align(Alignment.End)
            ) {
                Button(
                    text = strings.cancel(),
                    onClick = onDismiss
                )

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    text = strings.yes(),
                    style = ButtonStyles.primary,
                    onClick = {
                        habitQueries.deleteById(habit.id)
                        navigator.popUntil { it is AppDashboardScreen }
                    }
                )
            }
        }
    }
}