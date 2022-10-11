package breakbadhabits.android.app.compose.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import breakbadhabits.android.app.R
import breakbadhabits.android.app.appDependencies
import breakbadhabits.android.app.feature.HabitDeletionFeature
import breakbadhabits.android.app.feature.HabitNameInputFeature
import breakbadhabits.android.app.feature.HabitUpdatingFeature
import breakbadhabits.android.app.resources.HabitIconResources
import breakbadhabits.android.app.utils.AlertDialogManager
import breakbadhabits.android.compose.ui.Button
import breakbadhabits.android.compose.ui.ErrorText
import breakbadhabits.android.compose.ui.IconData
import breakbadhabits.android.compose.ui.IconsSelection
import breakbadhabits.android.compose.ui.InteractionType
import breakbadhabits.android.compose.ui.Text
import breakbadhabits.android.compose.ui.TextField
import breakbadhabits.android.compose.ui.Title
import epicarchitect.epicstore.compose.rememberEpicStoreEntry

@Composable
fun HabitEditingScreen(
    habitId: Int,
    onFinished: () -> Unit,
    onHabitDeleted: () -> Unit
) {
    val habitIconResources: HabitIconResources = appDependencies.habitIconResources
    val alertDialogManager: AlertDialogManager = appDependencies.alertDialogManager
    val habitDeletionFeature = rememberEpicStoreEntry {
        appDependencies.createHabitDeletionFeature(habitId)
    }
    val habitNameInputFeature = rememberEpicStoreEntry {
        appDependencies.createHabitNameInputFeature(habitId)
    }
    val habitUpdatingFeature = rememberEpicStoreEntry {
        appDependencies.createHabitUpdatingFeature(habitId)
    }
    val habitIconSelectionFeature = rememberEpicStoreEntry {
        appDependencies.createHabitIconSelectionFeature(habitId)
    }

    val habitName by habitNameInputFeature.input.collectAsState()
    val habitNameValidation by habitNameInputFeature.validation.collectAsState()
    val selectedIcon by habitIconSelectionFeature.selection.collectAsState()
    val habitUpdating by habitUpdatingFeature.state.collectAsState()
    val habitDeletion by habitDeletionFeature.state.collectAsState()

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val habitNameValidationResult =
        (habitNameValidation as? HabitNameInputFeature.ValidationState.Executed)?.result

    val habitUpdatingAllowed = (habitNameInputFeature.initialInput != habitName
            || habitIconSelectionFeature.initialValue != selectedIcon)
            && habitNameValidationResult is HabitNameInputFeature.ValidationResult.Valid

    if (habitUpdating is HabitUpdatingFeature.State.Executed) {
        LaunchedEffect(true) {
            onFinished()
        }
    }

    if (habitDeletion is HabitDeletionFeature.State.Executed) {
        LaunchedEffect(true) {
            onHabitDeleted()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Title(
            modifier = Modifier.padding(start = 18.dp, top = 18.dp, end = 18.dp, bottom = 4.dp),
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
            onValueChange = habitNameInputFeature::changeInput,
            label = stringResource(R.string.habitEditing_habitName),
            isError = habitNameValidation is HabitNameInputFeature.ValidationState.Executed
                    && (habitNameValidation as? HabitNameInputFeature.ValidationState.Executed)
                ?.result !is HabitNameInputFeature.ValidationResult.Valid
        )

        (habitNameValidation as? HabitNameInputFeature.ValidationState.Executed)?.let {
            when (it.result) {
                is HabitNameInputFeature.ValidationResult.Empty -> {
                    ErrorText(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp),
                        text = stringResource(R.string.habitEditing_habitNameValidation_empty),
                    )
                }
                is HabitNameInputFeature.ValidationResult.TooLong -> {
                    ErrorText(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp),
                        text = stringResource(
                            R.string.habitEditing_habitNameValidation_tooLong,
                            it.result.maxHabitNameLength
                        ),
                    )
                }
                is HabitNameInputFeature.ValidationResult.Used -> {
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
                habitIconSelectionFeature.changeSelection(it.id)
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
                        habitDeletionFeature.startDeletion()
                    },
                )
            },
            text = stringResource(R.string.habitEditing_deletion_button),
            interactionType = InteractionType.DANGEROUS
        )

        Spacer(modifier = Modifier.weight(1.0f))

        Button(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.End),
            onClick = {
                habitUpdatingFeature.startUpdating(
                    habitName!!,
                    selectedIcon
                )
            },
            enabled = habitUpdatingAllowed,
            text = stringResource(R.string.habitEditing_finish),
            interactionType = InteractionType.MAIN
        )
    }
}
