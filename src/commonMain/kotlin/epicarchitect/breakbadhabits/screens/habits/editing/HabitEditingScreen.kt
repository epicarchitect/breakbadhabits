package epicarchitect.breakbadhabits.screens.habits.editing

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.environment.LocalAppEnvironment
import epicarchitect.breakbadhabits.habits.checkHabitNewName
import epicarchitect.breakbadhabits.screens.root.LocalRootNavController
import epicarchitect.breakbadhabits.screens.root.RootRoute
import epicarchitect.breakbadhabits.uikit.InputCard
import epicarchitect.breakbadhabits.uikit.SimpleScrollableScreen
import epicarchitect.breakbadhabits.uikit.SingleSelectionGrid
import epicarchitect.breakbadhabits.uikit.TextInputCard

@Composable
fun HabitEditingScreen(habitId: Int? = null) {
    val state = rememberHabitEditingScreenState(habitId) ?: return
    val environment = LocalAppEnvironment.current
    val navController = LocalRootNavController.current
    val strings = environment.resources.strings.habitEditingStrings
    val habitIcons = environment.habits.icons
    val habitQueries = environment.database.habitQueries

    var showDeletion by remember { mutableStateOf(false) }
    if (showDeletion) {
        HabitDeletionDialog(
            habitId = habitId!!,
            onDismiss = { showDeletion = false },
            onDeleted = {
                navController.popBackStack(
                    route = RootRoute.Dashboard,
                    inclusive = false
                )
            }
        )
    }

    SimpleScrollableScreen(
        title = strings.titleText(isNewHabit = habitId == null),
        onBackClick = navController::popBackStack
    ) {
        Spacer(Modifier.height(16.dp))

        TextInputCard(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            value = state.habitName,
            onValueChange = {
                state.habitName = it
                state.habitNameError = null
            },
            title = strings.habitNameTitle(),
            description = strings.habitNameDescription(),
            error = state.habitNameError?.let(strings::habitNameError)
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
                items = habitIcons.items,
                selectedItem = habitIcons.getById(state.selectedIconId),
                cell = { icon ->
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = icon.imageVector,
                        contentDescription = null
                    )
                },
                onSelect = {
                    state.selectedIconId = it.id
                }
            )
        }

        Spacer(Modifier.height(16.dp))

        if (state.initialHabit != null) {
            OutlinedButton(
                modifier = Modifier.padding(horizontal = 16.dp),
                onClick = {
                    showDeletion = true
                }
            ) {
                Text(
                    text = strings.deleteButton()
                )
            }
        }

        Spacer(modifier = Modifier.weight(1.0f))

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.End),
            onClick = {
                state.habitNameError = checkHabitNewName(
                    newName = state.habitName,
                    initialName = state.initialHabit?.name,
                    maxLength = environment.habits.rules.maxHabitNameLength,
                    nameIsExists = { habitQueries.countWithName(it).executeAsOne() > 0L }
                )
                if (state.habitNameError != null) return@Button

                if (state.initialHabit != null) {
                    habitQueries.update(
                        id = state.initialHabit.id,
                        name = state.habitName,
                        iconId = state.selectedIconId
                    )
                } else {
                    habitQueries.insert(
                        id = null,
                        name = state.habitName,
                        iconId = state.selectedIconId
                    )
                }

                navController.popBackStack()
            }
        ) {
            Text(
                text = strings.finishButtonText()
            )
        }

        Spacer(Modifier.height(16.dp))
    }
}