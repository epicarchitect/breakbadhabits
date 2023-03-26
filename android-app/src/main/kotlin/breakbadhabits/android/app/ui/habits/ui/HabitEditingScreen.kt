package breakbadhabits.android.app.ui.habits.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import breakbadhabits.android.app.R
import breakbadhabits.android.app.ui.app.LocalHabitIconResourceProvider
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.logic.habits.IncorrectHabitNewName
import breakbadhabits.app.logic.habits.ValidatedHabitNewName
import breakbadhabits.foundation.controller.SingleRequestController
import breakbadhabits.foundation.controller.SingleSelectionController
import breakbadhabits.foundation.controller.ValidatedInputController
import breakbadhabits.foundation.uikit.LocalResourceIcon
import breakbadhabits.foundation.uikit.SingleSelectionGrid
import breakbadhabits.foundation.uikit.button.Button
import breakbadhabits.foundation.uikit.button.RequestButton
import breakbadhabits.foundation.uikit.effect.ClearFocusWhenKeyboardHiddenEffect
import breakbadhabits.foundation.uikit.text.Text
import breakbadhabits.foundation.uikit.text.TextFieldAdapter
import breakbadhabits.foundation.uikit.text.ValidatedInputField

@Composable
fun HabitEditingScreen(
    habitNameController: ValidatedInputController<Habit.Name, ValidatedHabitNewName>,
    habitIconSelectionController: SingleSelectionController<Habit.Icon>,
    updatingController: SingleRequestController,
    deletionController: SingleRequestController,
) {
    val context = LocalContext.current
    val habitIconResources = LocalHabitIconResourceProvider.current

    ClearFocusWhenKeyboardHiddenEffect()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Spacer(Modifier.height(24.dp))

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

        ValidatedInputField(
            controller = habitNameController,
            adapter = remember {
                TextFieldAdapter(
                    decodeInput = Habit.Name::value,
                    encodeInput = Habit::Name,
                    extractErrorMessage = {
                        val incorrect = (it as? IncorrectHabitNewName)
                            ?: return@TextFieldAdapter null
                        when (incorrect.reason) {
                            is IncorrectHabitNewName.Reason.AlreadyUsed -> {
                                context.getString(R.string.habitEditing_habitNameValidation_used)
                            }

                            is IncorrectHabitNewName.Reason.Empty -> {
                                context.getString(R.string.habitEditing_habitNameValidation_empty)
                            }

                            is IncorrectHabitNewName.Reason.TooLong -> {
                                context.getString(
                                    R.string.habitEditing_habitNameValidation_tooLong,
                                    (incorrect.reason as IncorrectHabitNewName.Reason.TooLong).maxLength
                                )
                            }
                        }
                    }
                )
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
                    resourceId = habitIconResources[icon].resourceId
                )
            }
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.habitEditing_deletion_description)
        )

        Spacer(modifier = Modifier.height(16.dp))

        RequestButton(
            requestController = deletionController,
            text = stringResource(R.string.habitEditing_deletion_button),
            type = Button.Type.Dangerous
        )

        Spacer(modifier = Modifier.weight(1.0f))

        Spacer(modifier = Modifier.height(48.dp))

        RequestButton(
            modifier = Modifier.align(Alignment.End),
            requestController = updatingController,
            text = stringResource(R.string.habitEditing_finish),
            type = Button.Type.Main
        )
    }
}