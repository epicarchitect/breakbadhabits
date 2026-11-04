package epicarchitect.breakbadhabits.screens.habits.editing

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.habits.HabitIcons
import epicarchitect.breakbadhabits.habits.HabitNewNameError
import epicarchitect.breakbadhabits.habits.HabitsRules
import epicarchitect.breakbadhabits.habits.checkHabitNewName
import epicarchitect.breakbadhabits.infrastructure.AppDatabase
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.back
import epicarchitect.breakbadhabits.resources.habit_delete_button
import epicarchitect.breakbadhabits.resources.habit_editing_title_editing
import epicarchitect.breakbadhabits.resources.habit_editing_title_new
import epicarchitect.breakbadhabits.resources.habit_icon_description
import epicarchitect.breakbadhabits.resources.habit_icon_title
import epicarchitect.breakbadhabits.resources.habit_name_description
import epicarchitect.breakbadhabits.resources.habit_name_error_already_used
import epicarchitect.breakbadhabits.resources.habit_name_error_empty
import epicarchitect.breakbadhabits.resources.habit_name_error_too_long
import epicarchitect.breakbadhabits.resources.habit_name_title
import epicarchitect.breakbadhabits.resources.save_changes
import epicarchitect.breakbadhabits.screens.root.LocalNavBackStack
import epicarchitect.breakbadhabits.uikit.InputCard
import epicarchitect.breakbadhabits.uikit.SingleSelectionGrid
import epicarchitect.breakbadhabits.uikit.TextInputCard
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.Uuid

@Composable
fun HabitEditingScreen(habitId: Uuid? = null) {
    val state = rememberHabitEditingScreenState(habitId) ?: return
    val navBackStack = LocalNavBackStack.current
    val habitIcons = HabitIcons
    val habitsDao = AppDatabase.habitsDao
    val coroutineScope = rememberCoroutineScope()
    var showDeletion by remember { mutableStateOf(false) }
    val editingHabitId = state.initialHabit?.id
    if (showDeletion && editingHabitId != null) {
        HabitDeletionDialog(
            habitId = editingHabitId,
            onDismiss = { showDeletion = false },
            onDeleted = {
                while (navBackStack.size > 1) navBackStack.removeLastOrNull()
            }
        )
    }

    val listState = rememberLazyListState()
    val bottomInset = WindowInsets.navigationBars
        .asPaddingValues()
        .calculateBottomPadding()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        contentPadding = PaddingValues(bottom = bottomInset),
        state = listState
    ) {
        item(key = "app_bar") {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = stringResource(
                            if (habitId == null) Res.string.habit_editing_title_new
                            else Res.string.habit_editing_title_editing
                        ),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navBackStack.removeLastOrNull() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }

        item(key = "content") {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(max = 600.dp)
                        .fillMaxWidth()
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
                        title = stringResource(Res.string.habit_name_title),
                        description = stringResource(Res.string.habit_name_description),
                        error = state.habitNameError?.let { error ->
                            when (error) {
                                HabitNewNameError.AlreadyUsed -> stringResource(
                                    Res.string.habit_name_error_already_used
                                )

                                HabitNewNameError.Empty -> stringResource(
                                    Res.string.habit_name_error_empty
                                )

                                is HabitNewNameError.TooLong -> stringResource(
                                    Res.string.habit_name_error_too_long,
                                    error.maxLength
                                )
                            }
                        }
                    )

                    Spacer(Modifier.height(16.dp))

                    InputCard(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        title = stringResource(Res.string.habit_icon_title),
                        description = stringResource(Res.string.habit_icon_description)
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
                                text = stringResource(Res.string.habit_delete_button)
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .align(Alignment.End),
                        onClick = {
                            coroutineScope.launch {
                                state.habitNameError = checkHabitNewName(
                                    newName = state.habitName,
                                    initialName = state.initialHabit?.name,
                                    maxLength = HabitsRules.maxHabitNameLength,
                                    nameIsExists = { habitsDao.countWithName(it) > 0L }
                                )
                                if (state.habitNameError != null) {
                                    return@launch
                                }

                                val habitId = state.initialHabit?.id ?: Uuid.generateV7()
                                if (state.initialHabit != null) {
                                    habitsDao.update(
                                        id = habitId,
                                        name = state.habitName,
                                        iconId = state.selectedIconId
                                    )
                                } else {
                                    habitsDao.insert(
                                        id = habitId,
                                        name = state.habitName,
                                        iconId = state.selectedIconId
                                    )
                                }

                                navBackStack.removeLastOrNull()
                            }
                        }
                    ) {
                        Text(
                            text = stringResource(Res.string.save_changes)
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                }
            }
        }
    }
}
