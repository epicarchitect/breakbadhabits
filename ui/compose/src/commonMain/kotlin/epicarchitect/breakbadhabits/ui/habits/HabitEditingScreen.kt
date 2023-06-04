package epicarchitect.breakbadhabits.ui.habits

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.foundation.controller.SingleRequestController
import epicarchitect.breakbadhabits.foundation.controller.SingleSelectionController
import epicarchitect.breakbadhabits.foundation.controller.ValidatedInputController
import epicarchitect.breakbadhabits.foundation.icons.Icon
import epicarchitect.breakbadhabits.foundation.uikit.Dialog
import epicarchitect.breakbadhabits.foundation.uikit.SingleSelectionGrid
import epicarchitect.breakbadhabits.foundation.uikit.button.Button
import epicarchitect.breakbadhabits.foundation.uikit.button.RequestButton
import epicarchitect.breakbadhabits.foundation.uikit.effect.ClearFocusWhenKeyboardHiddenEffect
import epicarchitect.breakbadhabits.foundation.uikit.text.Text
import epicarchitect.breakbadhabits.foundation.uikit.text.TextFieldValidationAdapter
import epicarchitect.breakbadhabits.foundation.uikit.text.ValidatedTextField
import epicarchitect.breakbadhabits.logic.habits.validator.IncorrectHabitNewName
import epicarchitect.breakbadhabits.logic.habits.validator.ValidatedHabitNewName

@Composable
fun HabitEditing(
    habitNameController: ValidatedInputController<String, ValidatedHabitNewName>,
    habitIconSelectionController: SingleSelectionController<Icon>,
    updatingController: SingleRequestController,
    deletionController: SingleRequestController,
) {

    ClearFocusWhenKeyboardHiddenEffect()

    var deletionShow by remember { mutableStateOf(false) }
    if (deletionShow) {
        Dialog(onDismiss = { deletionShow = false }) {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                Text(
                    text = "stringResource(R.string.habit_deleteConfirmation)",
                    type = Text.Type.Description,
                    priority = Text.Priority.High
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Button(
                        text = "stringResource(R.string.cancel)",
                        onClick = {
                            deletionShow = false
                        }
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    RequestButton(
                        controller = deletionController,
                        text = "stringResource(R.string.yes)",
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
            text = "stringResource(R.string.habitEditing_title)",
            type = Text.Type.Title,
            priority = Text.Priority.High
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "stringResource(R.string.habitEditing_habitName_description)"
        )

        Spacer(Modifier.height(16.dp))

        ValidatedTextField(
            controller = habitNameController,
            validationAdapter = TextFieldValidationAdapter {
                if (it !is IncorrectHabitNewName) null
                else when (val reason = it.reason) {
                    is IncorrectHabitNewName.Reason.AlreadyUsed -> {
                        "context.getString(R.string.habitEditing_habitNameValidation_used)"
                    }

                    is IncorrectHabitNewName.Reason.Empty -> {
                        "context.getString(R.string.habitEditing_habitNameValidation_empty)"
                    }

                    is IncorrectHabitNewName.Reason.TooLong -> {
                        "context.getString(R.string.habitEditing_habitNameValidation_tooLong, reason.maxLength)"
                    }
                }
            },
            label = "stringResource(R.string.habitEditing_habitName)"
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "stringResource(R.string.habitEditing_habitIcon_description)"
        )

        Spacer(Modifier.height(16.dp))

        SingleSelectionGrid(
            controller = habitIconSelectionController,
            cell = { icon ->
//                LocalResourceIcon(
//                    modifier = Modifier.size(24.dp),
//                    resourceId = icon.resourceId
//                )
            }
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "stringResource(R.string.habitEditing_deletion_description)"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            text = "stringResource(R.string.habitEditing_deletion_button)",
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
            text = "stringResource(R.string.habitEditing_finish)",
            type = Button.Type.Main,
//            icon = {
//                LocalResourceIcon(resourceId = R.drawable.ic_done)
//            }
        )
    }
}