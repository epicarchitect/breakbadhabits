package breakbadhabits.android.app.ui.habits

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import breakbadhabits.android.app.R
import breakbadhabits.android.app.icons.resourceId
import breakbadhabits.app.logic.habits.IncorrectHabitNewName
import breakbadhabits.app.logic.habits.ValidatedHabitNewName
import breakbadhabits.app.logic.icons.LocalIcon
import breakbadhabits.foundation.controller.SingleRequestController
import breakbadhabits.foundation.controller.SingleSelectionController
import breakbadhabits.foundation.controller.ValidatedInputController
import breakbadhabits.foundation.uikit.Dialog
import breakbadhabits.foundation.uikit.LocalResourceIcon
import breakbadhabits.foundation.uikit.SingleSelectionGrid
import breakbadhabits.foundation.uikit.button.Button
import breakbadhabits.foundation.uikit.button.RequestButton
import breakbadhabits.foundation.uikit.effect.ClearFocusWhenKeyboardHiddenEffect
import breakbadhabits.foundation.uikit.text.Text
import breakbadhabits.foundation.uikit.text.TextFieldValidationAdapter
import breakbadhabits.foundation.uikit.text.ValidatedTextField

@Composable
fun HabitEditingScreen(
    habitNameController: ValidatedInputController<String, ValidatedHabitNewName>,
    habitIconSelectionController: SingleSelectionController<LocalIcon>,
    updatingController: SingleRequestController,
    deletionController: SingleRequestController,
) {
    val context = LocalContext.current

    ClearFocusWhenKeyboardHiddenEffect()

    var deletionShow by remember { mutableStateOf(false) }
    if (deletionShow) {
        Dialog(onDismiss = { deletionShow = false }) {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                Text(
                    text = stringResource(R.string.habit_deleteConfirmation),
                    type = Text.Type.Description,
                    priority = Text.Priority.High
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Button(
                        text = stringResource(R.string.cancel),
                        onClick = {
                            deletionShow = false
                        }
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    RequestButton(
                        controller = deletionController,
                        text = stringResource(R.string.yes),
                        type = Button.Type.Main
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
            text = stringResource(R.string.habitEditing_title),
            type = Text.Type.Title,
            priority = Text.Priority.High
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.habitEditing_habitName_description)
        )

        Spacer(Modifier.height(16.dp))

        ValidatedTextField(
            controller = habitNameController,
            validationAdapter = TextFieldValidationAdapter {
                if (it !is IncorrectHabitNewName) null
                else when (val reason = it.reason) {
                    is IncorrectHabitNewName.Reason.AlreadyUsed -> {
                        context.getString(R.string.habitEditing_habitNameValidation_used)
                    }

                    is IncorrectHabitNewName.Reason.Empty -> {
                        context.getString(R.string.habitEditing_habitNameValidation_empty)
                    }

                    is IncorrectHabitNewName.Reason.TooLong -> {
                        context.getString(
                            R.string.habitEditing_habitNameValidation_tooLong,
                            reason.maxLength
                        )
                    }
                }
            },
            label = stringResource(R.string.habitEditing_habitName)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.habitEditing_habitIcon_description)
        )

        Spacer(Modifier.height(16.dp))

        SingleSelectionGrid(
            controller = habitIconSelectionController,
            cell = { icon ->
                LocalResourceIcon(
                    modifier = Modifier.size(24.dp),
                    resourceId = icon.resourceId
                )
            }
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.habitEditing_deletion_description)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            text = stringResource(R.string.habitEditing_deletion_button),
            type = Button.Type.Dangerous,
            onClick = {
                deletionShow = true
            }
        )

        Spacer(modifier = Modifier.weight(1.0f))

        Spacer(modifier = Modifier.height(48.dp))

        RequestButton(
            modifier = Modifier.align(Alignment.End),
            controller = updatingController,
            text = stringResource(R.string.habitEditing_finish),
            type = Button.Type.Main,
            icon = {
                LocalResourceIcon(resourceId = R.drawable.ic_done)
            }
        )
    }
}