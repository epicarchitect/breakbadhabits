package epicarchitect.breakbadhabits.features.habits.editing

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
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import epicarchitect.breakbadhabits.HabitsConfigProvider
import epicarchitect.breakbadhabits.foundation.icons.get
import epicarchitect.breakbadhabits.foundation.uikit.Dialog
import epicarchitect.breakbadhabits.foundation.uikit.Icon
import epicarchitect.breakbadhabits.foundation.uikit.SingleSelectionGrid
import epicarchitect.breakbadhabits.foundation.uikit.button.Button
import epicarchitect.breakbadhabits.foundation.uikit.effect.ClearFocusWhenKeyboardHiddenEffect
import epicarchitect.breakbadhabits.foundation.uikit.ext.onFocusLost
import epicarchitect.breakbadhabits.foundation.uikit.text.Text
import epicarchitect.breakbadhabits.foundation.uikit.text.TextField
import epicarchitect.breakbadhabits.ui.icons.VectorIcons
import epicarchitect.breakbadhabits.validator.CorrectHabitNewName
import epicarchitect.breakbadhabits.validator.HabitNewNameValidator
import epicarchitect.breakbadhabits.validator.IncorrectHabitNewName
import epicarchitect.breakbadhabits.validator.ValidatedHabitNewName
import kotlinx.coroutines.Dispatchers

@Composable
fun HabitEditing(dependencies: HabitEditingDependencies) {
    val initialHabit by remember(dependencies) {
        dependencies.mainDatabase.habitQueries
            .selectById(dependencies.habitId)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
    }.collectAsState(null)

    var habitName by rememberSaveable(initialHabit) { mutableStateOf(initialHabit?.name ?: "") }
    var validatedHabitName by remember { mutableStateOf<ValidatedHabitNewName?>(null) }

    var selectedIconId by rememberSaveable(initialHabit) { mutableIntStateOf(initialHabit?.iconId ?: 0) }
    val selectedIcon = remember(selectedIconId) { dependencies.habitIcons[selectedIconId] }

    ClearFocusWhenKeyboardHiddenEffect()

    var deletionShow by remember { mutableStateOf(false) }
    if (deletionShow) {
        Dialog(onDismiss = { deletionShow = false }) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = dependencies.resources.deleteConfirmation(),
                    type = Text.Type.Description,
                    priority = Text.Priority.High
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Button(
                        text = dependencies.resources.cancel(),
                        onClick = {
                            deletionShow = false
                        }
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        text = dependencies.resources.yes(),
                        type = Button.Type.Main,
                        onClick = {
                            dependencies.mainDatabase.habitQueries.deleteById(dependencies.habitId)
                            dependencies.navigation.backToDashboard()
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
            text = dependencies.resources.titleText(),
            type = Text.Type.Title,
            priority = Text.Priority.High
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = dependencies.resources.habitNameDescription()
        )

        Spacer(Modifier.height(16.dp))

        TextField(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .onFocusLost {
                    validatedHabitName = HabitNewNameValidator(
                        mainDatabase = dependencies.mainDatabase,
                        configProvider = HabitsConfigProvider()
                    ).validate(habitName)
                },
            value = habitName,
            onValueChange = {
                habitName = it
            },
            label = dependencies.resources.habitNameLabel(),
            error = (validatedHabitName as? IncorrectHabitNewName)?.let {
                dependencies.resources.habitNameValidationError(it.reason)
            },
            description = dependencies.resources.habitNameDescription()
        )

        Spacer(Modifier.height(24.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = dependencies.resources.habitIconDescription(),
            type = Text.Type.Description,
            priority = Text.Priority.Medium
        )

        Spacer(Modifier.height(12.dp))

        SingleSelectionGrid(
            modifier = Modifier.padding(horizontal = 16.dp),
            items = dependencies.habitIcons.list,
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
            text = dependencies.resources.deleteDescription()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            text = dependencies.resources.deleteButton(),
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
                dependencies.mainDatabase.habitQueries.update(
                    id = dependencies.habitId,
                    name = (validatedHabitName as CorrectHabitNewName).data,
                    iconId = selectedIconId
                )
                dependencies.navigation.back()
            },
            text = dependencies.resources.finishButtonText(),
            type = Button.Type.Main,
            icon = { Icon(VectorIcons.Done) },
            enabled = validatedHabitName is CorrectHabitNewName
        )

        Spacer(Modifier.height(16.dp))
    }
}