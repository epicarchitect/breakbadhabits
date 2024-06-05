package epicarchitect.breakbadhabits.ui.screen.habits.editing

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
import epicarchitect.breakbadhabits.operation.habits.validation.HabitNewNameIncorrectReason
import epicarchitect.breakbadhabits.operation.habits.validation.habitNewNameIncorrectReason
import epicarchitect.breakbadhabits.operation.sqldelight.flowOfOneOrNull
import epicarchitect.breakbadhabits.ui.component.Dialog
import epicarchitect.breakbadhabits.ui.component.Icon
import epicarchitect.breakbadhabits.ui.component.SimpleTopAppBar
import epicarchitect.breakbadhabits.ui.component.SingleSelectionGrid
import epicarchitect.breakbadhabits.ui.component.button.Button
import epicarchitect.breakbadhabits.ui.component.text.Text
import epicarchitect.breakbadhabits.ui.component.text.TextField
import epicarchitect.breakbadhabits.ui.screen.dashboard.DashboardScreen

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
    val strings = AppData.resources.strings.habitEditingStrings
    val icons = AppData.resources.icons

    val initialHabit by remember(habitId) { habitQueries.habitById(habitId).flowOfOneOrNull() }.collectAsState(null)

    var habitName by rememberSaveable(initialHabit) { mutableStateOf(initialHabit?.name ?: "") }
    var habitNameIncorrectReason by remember { mutableStateOf<HabitNewNameIncorrectReason?>(null) }
    var selectedIconId by rememberSaveable(initialHabit) { mutableIntStateOf(initialHabit?.iconId ?: 0) }

    var deletionShow by remember { mutableStateOf(false) }
    if (deletionShow) {
        Dialog(onDismiss = { deletionShow = false }) {
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
                        onClick = {
                            deletionShow = false
                        }
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        text = strings.yes(),
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
    ) {
        SimpleTopAppBar(
            title = strings.titleText(),
            onBackClick = navigator::pop,
        )

        Spacer(Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = strings.habitNameDescription()
        )

        Spacer(Modifier.height(16.dp))

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
            modifier = Modifier.padding(horizontal = 16.dp),
            text = strings.deleteDescription()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = strings.deleteButton(),
            type = Button.Type.Dangerous,
            onClick = {
                deletionShow = true
            }
        )

        Spacer(modifier = Modifier.weight(1.0f))

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.End),
            onClick = {
                habitNameIncorrectReason = habitName.habitNewNameIncorrectReason(
                    initialName = initialHabit!!.name,
                    maxLength = AppData.habitsConfig.maxHabitNameLength,
                    nameIsExists = { AppData.database.habitQueries.countWithName(it).executeAsOne() > 0L }
                )
                if (habitNameIncorrectReason != null) return@Button

                habitQueries.update(
                    id = habitId,
                    name = habitName,
                    iconId = selectedIconId
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