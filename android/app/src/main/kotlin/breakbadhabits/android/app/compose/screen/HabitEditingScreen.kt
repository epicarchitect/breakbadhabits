package breakbadhabits.android.app.compose.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import breakbadhabits.android.app.App
import breakbadhabits.android.app.R
import breakbadhabits.android.app.viewmodel.HabitDeletionViewModel
import breakbadhabits.android.app.viewmodel.HabitEditingViewModel
import breakbadhabits.android.compose.molecule.ActionType
import breakbadhabits.android.compose.molecule.Button
import breakbadhabits.android.compose.molecule.ErrorText
import breakbadhabits.android.compose.molecule.Text
import breakbadhabits.android.compose.molecule.TextField
import breakbadhabits.android.compose.molecule.Title
import breakbadhabits.compose.organism.icons.selection.IconData
import breakbadhabits.compose.organism.icons.selection.IconsSelection

@Composable
fun HabitEditingScreen(
    habitId: Int,
    onFinished: () -> Unit,
    onHabitDeleted: () -> Unit
) {
    val habitEditingViewModel = viewModel {
        App.architecture.createHabitEditingViewModel(habitId)
    }
    val habitDeletionViewModel = viewModel {
        App.architecture.createHabitDeletionViewModel(habitId)
    }
    val habitIconResources = App.architecture.habitIconResources
    val alertDialogManager = App.architecture.alertDialogManager

    val habitName by habitEditingViewModel.habitNameStateFlow.collectAsState()
    val selectedIcon by habitEditingViewModel.habitIconIdStateFlow.collectAsState()
    val habitNameValidation by habitEditingViewModel.habitNameValidationStateFlow.collectAsState()
    val habitUpdatingAllowed by habitEditingViewModel.habitUpdatingAllowedStateFlow.collectAsState()
    val habitCreation by habitEditingViewModel.habitUpdatingStateFlow.collectAsState()
    val habitDeletion by habitDeletionViewModel.habitDeleteStateFlow.collectAsState()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    if (habitCreation is HabitEditingViewModel.HabitUpdatingState.Executed) {
        LaunchedEffect(true) {
            onFinished()
        }
    }

    if (habitDeletion is HabitDeletionViewModel.HabitDeleteState.Executed) {
        LaunchedEffect(true) {
            onHabitDeleted()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .padding(WindowInsets.systemBars.asPaddingValues())
                .fillMaxSize()
        ) {
            Title(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 4.dp),
                text = stringResource(R.string.habitEditing_title)
            )

            Text(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                text = stringResource(R.string.habitEditing_habitName_description)
            )

            TextField(
                modifier = Modifier
                    .padding(start = 16.dp, top = 8.dp, end = 16.dp)
                    .fillMaxWidth(),
                value = habitName ?: "",
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                onValueChange = {
                    habitEditingViewModel.updateHabitName(it)
                },
                label = stringResource(R.string.habitEditing_habitName),
                isError = habitNameValidation is HabitEditingViewModel.HabitNameValidationState.Executed
                        && (habitNameValidation as? HabitEditingViewModel.HabitNameValidationState.Executed)
                    ?.result !is HabitEditingViewModel.HabitNameValidationResult.Valid
            )

            (habitNameValidation as? HabitEditingViewModel.HabitNameValidationState.Executed)?.let {
                when (it.result) {
                    is HabitEditingViewModel.HabitNameValidationResult.Empty -> {
                        ErrorText(
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp),
                            text = stringResource(R.string.habitEditing_habitNameValidation_empty)
                        )
                    }
                    is HabitEditingViewModel.HabitNameValidationResult.TooLong -> {
                        ErrorText(
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp),
                            text = stringResource(R.string.habitEditing_habitNameValidation_tooLong, it.result.maxHabitNameLength),
                        )
                    }
                    is HabitEditingViewModel.HabitNameValidationResult.Used -> {
                        ErrorText(
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp),
                            text = stringResource(R.string.habitEditing_habitNameValidation_used),
                        )
                    }
                    else -> {
                        /* no-op */
                    }
                }
            }

            Text(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                text = stringResource(R.string.habitEditing_habitIcon_description)
            )

            IconsSelection(
                modifier = Modifier
                    .padding(start = 16.dp, top = 8.dp, end = 16.dp)
                    .fillMaxWidth(),
                icons = habitIconResources.icons.map {
                    IconData(
                        it.iconId,
                        it.resourceId
                    )
                },
                selectedIcon = habitIconResources.icons.first { it.iconId == selectedIcon }.let {
                    IconData(
                        it.iconId,
                        it.resourceId
                    )
                },
                onSelect = {
                    habitEditingViewModel.updateHabitIconId(it.id)
                }
            )

            Text(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                text = stringResource(R.string.habitEditing_deletion_description)
            )

            Button(
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
                onClick = {
                    alertDialogManager.showAlert(
                        context,
                        title = null,
                        message = context.getString(R.string.habit_deleteConfirmation),
                        positiveButtonTitle = context.getString(R.string.yes),
                        negativeButtonTitle = context.getString(R.string.cancel),
                        onPositive = {
                            habitDeletionViewModel.deleteHabit()
                        },
                    )
                },
                text = stringResource(R.string.habitEditing_deletion_button),
                actionType = ActionType.DANGEROUS
            )

            Spacer(modifier = Modifier.weight(1.0f))

            Button(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.End),
                onClick = {
                    habitEditingViewModel.saveHabit()
                },
                enabled = habitUpdatingAllowed,
                text = stringResource(R.string.habitEditing_finish),
                actionType = ActionType.MAIN
            )
        }
    }
}
