package epicarchitect.breakbadhabits.screens.habits.editing

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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.HabitsConfigProvider
import epicarchitect.breakbadhabits.database.AppData
import epicarchitect.breakbadhabits.icons.HabitIcons
import epicarchitect.breakbadhabits.icons.VectorIcons
import epicarchitect.breakbadhabits.screens.dashboard.DashboardScreen
import epicarchitect.breakbadhabits.uikit.Dialog
import epicarchitect.breakbadhabits.uikit.Icon
import epicarchitect.breakbadhabits.uikit.SingleSelectionGrid
import epicarchitect.breakbadhabits.uikit.button.Button
import epicarchitect.breakbadhabits.uikit.effect.ClearFocusWhenKeyboardHiddenEffect
import epicarchitect.breakbadhabits.uikit.ext.onFocusLost
import epicarchitect.breakbadhabits.uikit.text.Text
import epicarchitect.breakbadhabits.uikit.text.TextField
import epicarchitect.breakbadhabits.validator.CorrectHabitNewName
import epicarchitect.breakbadhabits.validator.HabitNewNameValidator
import epicarchitect.breakbadhabits.validator.IncorrectHabitNewName
import epicarchitect.breakbadhabits.validator.ValidatedHabitNewName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

class HabitEditingScreen(private val habitId: Int) : Screen {
    @Composable
    override fun Content() {
        HabitEditing(habitId)
    }
}

@Composable
fun HabitEditing(habitId: Int) {
    val navigator = LocalNavigator.currentOrThrow
    val resources = LocalHabitEditingResources.current

    val initialHabit by remember(habitId) {
        AppData.mainDatabase.habitQueries
            .selectById(habitId)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
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
                    text = resources.deleteConfirmation(),
                    type = Text.Type.Description,
                    priority = Text.Priority.High
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Button(
                        text = resources.cancel(),
                        onClick = {
                            deletionShow = false
                        }
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        text = resources.yes(),
                        type = Button.Type.Main,
                        onClick = {
                            AppData.mainDatabase.habitQueries.deleteById(habitId)
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
            text = resources.titleText(),
            type = Text.Type.Title,
            priority = Text.Priority.High
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = resources.habitNameDescription()
        )

        Spacer(Modifier.height(16.dp))

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
            selectedItem = HabitIcons[selectedIconId],
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
            text = resources.deleteDescription()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            text = resources.deleteButton(),
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
                AppData.mainDatabase.habitQueries.update(
                    id = habitId,
                    name = (validatedHabitName as CorrectHabitNewName).data,
                    iconId = selectedIconId
                )
                navigator.pop()
            },
            text = resources.finishButtonText(),
            type = Button.Type.Main,
            icon = { Icon(VectorIcons.Done) },
            enabled = validatedHabitName is CorrectHabitNewName
        )

        Spacer(Modifier.height(16.dp))
    }
}