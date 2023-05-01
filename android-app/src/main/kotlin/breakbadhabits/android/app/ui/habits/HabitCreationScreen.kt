package breakbadhabits.android.app.ui.habits

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import breakbadhabits.android.app.R
import breakbadhabits.android.app.di.LocalLogicModule
import breakbadhabits.android.app.di.LocalUiModule
import breakbadhabits.android.app.icons.resourceId
import breakbadhabits.app.logic.habits.validator.IncorrectHabitNewName
import breakbadhabits.app.logic.habits.validator.IncorrectHabitTrackEventCount
import breakbadhabits.app.logic.habits.validator.ValidatedHabitNewName
import breakbadhabits.app.logic.habits.validator.ValidatedHabitTrackEventCount
import breakbadhabits.app.logic.habits.validator.ValidatedHabitTrackTime
import breakbadhabits.app.logic.icons.LocalIcon
import breakbadhabits.foundation.controller.SingleRequestController
import breakbadhabits.foundation.controller.SingleSelectionController
import breakbadhabits.foundation.controller.ValidatedInputController
import breakbadhabits.foundation.datetime.InstantRange
import breakbadhabits.foundation.datetime.withZeroSeconds
import breakbadhabits.foundation.uikit.LocalResourceIcon
import breakbadhabits.foundation.uikit.SingleSelectionChipRow
import breakbadhabits.foundation.uikit.SingleSelectionGrid
import breakbadhabits.foundation.uikit.button.Button
import breakbadhabits.foundation.uikit.button.RequestButton
import breakbadhabits.foundation.uikit.effect.ClearFocusWhenKeyboardHiddenEffect
import breakbadhabits.foundation.uikit.regex.Regexps
import breakbadhabits.foundation.uikit.text.Text
import breakbadhabits.foundation.uikit.text.TextFieldInputAdapter
import breakbadhabits.foundation.uikit.text.TextFieldValidationAdapter
import breakbadhabits.foundation.uikit.text.ValidatedInputField
import breakbadhabits.foundation.uikit.text.ValidatedTextField
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

private enum class HabitTime(
    val titleRes: Int,
    val offset: Duration
) {
    MONTH_1(R.string.habitCreation_habitTime_month_1, 30.days),
    MONTH_3(R.string.habitCreation_habitTime_month_3, 90.days),
    MONTH_6(R.string.habitCreation_habitTime_month_6, 180.days),
    YEAR_1(R.string.habitCreation_habitTime_year_1, 365.days),
    YEAR_2(R.string.habitCreation_habitTime_year_2, 365.days * 2),
    YEAR_3(R.string.habitCreation_habitTime_year_3, 365.days * 3),
    YEAR_4(R.string.habitCreation_habitTime_year_4, 365.days * 4),
    YEAR_5(R.string.habitCreation_habitTime_year_5, 365.days * 5),
    YEAR_6(R.string.habitCreation_habitTime_year_6, 365.days * 6),
    YEAR_7(R.string.habitCreation_habitTime_year_7, 365.days * 7),
    YEAR_8(R.string.habitCreation_habitTime_year_8, 365.days * 8),
    YEAR_9(R.string.habitCreation_habitTime_year_9, 365.days * 9),
    YEAR_10(R.string.habitCreation_habitTime_year_10, 365.days * 10)
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HabitCreationScreen(
    habitIconSelectionController: SingleSelectionController<LocalIcon>,
    habitNameController: ValidatedInputController<String, ValidatedHabitNewName>,
    dailyEventCountInputController: ValidatedInputController<Int, ValidatedHabitTrackEventCount>,
    firstTrackTimeInputController: ValidatedInputController<InstantRange, ValidatedHabitTrackTime>,
    creationController: SingleRequestController
) {
    val logicModule = LocalLogicModule.current
    val context = LocalContext.current
    val currentTime by logicModule.dateTimeProvider.currentTime.collectAsState()
    val timeZone by logicModule.dateTimeProvider.timeZone.collectAsState()

    var selectedHabitTimeIndex by rememberSaveable {
        mutableStateOf(0)
    }

    LaunchedEffect(selectedHabitTimeIndex) {
        val item = HabitTime.values()[selectedHabitTimeIndex]
        val range = (currentTime - item.offset)..currentTime
        firstTrackTimeInputController.changeInput(
            range.withZeroSeconds(timeZone)
        )
    }

    ClearFocusWhenKeyboardHiddenEffect()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(R.string.habitCreation_title),
            type = Text.Type.Title,
            priority = Text.Priority.High
        )

        Spacer(Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(R.string.habitCreation_habitName_description),
            type = Text.Type.Description,
            priority = Text.Priority.Medium
        )

        Spacer(Modifier.height(12.dp))

        ValidatedTextField(
            modifier = Modifier.padding(horizontal = 16.dp),
            controller = habitNameController,
            validationAdapter = remember {
                TextFieldValidationAdapter {
                    if (it !is IncorrectHabitNewName) null
                    else when (val reason = it.reason) {
                        is IncorrectHabitNewName.Reason.AlreadyUsed -> {
                            context.getString(R.string.habitCreation_habitNameValidation_used)
                        }

                        is IncorrectHabitNewName.Reason.Empty -> {
                            context.getString(R.string.habitCreation_habitNameValidation_empty)
                        }

                        is IncorrectHabitNewName.Reason.TooLong -> {
                            context.getString(
                                R.string.habitCreation_habitNameValidation_tooLong,
                                reason.maxLength
                            )
                        }
                    }
                }
            },
            label = "Название"//stringResource(R.string.habitCreation_habitName)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(R.string.habitCreation_habitIcon_description),
            type = Text.Type.Description,
            priority = Text.Priority.Medium
        )

        Spacer(Modifier.height(12.dp))

        SingleSelectionGrid(
            modifier = Modifier.padding(horizontal = 16.dp),
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
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Укажите примерно как давно у вас эта привычка:",
            type = Text.Type.Description,
            priority = Text.Priority.Medium
        )

        Spacer(Modifier.height(12.dp))

        SingleSelectionChipRow(
            items = HabitTime.values().map {
                context.getString(it.titleRes)
            },
            onClick = {
                selectedHabitTimeIndex = it
            },
            selectedIndex = selectedHabitTimeIndex,
            edgePadding = 16.dp
        )

        Spacer(Modifier.height(24.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Укажите сколько примерно было событий привычки каждый день:",
            type = Text.Type.Description,
            priority = Text.Priority.Medium
        )

        Spacer(Modifier.height(12.dp))

        ValidatedInputField(
            modifier = Modifier.padding(horizontal = 16.dp),
            controller = dailyEventCountInputController,
            inputAdapter = remember {
                TextFieldInputAdapter(
                    decodeInput = { it.toString() },
                    encodeInput = { it.toIntOrNull() ?: 0 }
                )
            },
            validationAdapter = remember {
                TextFieldValidationAdapter {
                    if (it !is IncorrectHabitTrackEventCount) null
                    else when (it.reason) {
                        is IncorrectHabitTrackEventCount.Reason.Empty -> {
                            "Поле не может быть пустым"
                        }
                    }
                }
            },
            label = "Число событий в день",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            regex = Regexps.integersOrEmpty(maxCharCount = 4)
        )

        Spacer(modifier = Modifier.weight(1.0f))

        Spacer(modifier = Modifier.height(24.dp))

        RequestButton(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.End),
            controller = creationController,
            text = stringResource(R.string.habitCreation_finish),
            type = Button.Type.Main,
            icon = {
                LocalResourceIcon(resourceId = R.drawable.ic_done)
            }
        )

        Spacer(Modifier.height(16.dp))
    }
}