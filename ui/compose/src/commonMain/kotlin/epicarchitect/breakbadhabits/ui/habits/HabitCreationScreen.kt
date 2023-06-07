package epicarchitect.breakbadhabits.ui.habits

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.di.holder.LocalAppModule
import epicarchitect.breakbadhabits.foundation.controller.SingleRequestController
import epicarchitect.breakbadhabits.foundation.controller.SingleSelectionController
import epicarchitect.breakbadhabits.foundation.controller.ValidatedInputController
import epicarchitect.breakbadhabits.foundation.datetime.ZonedDateTimeRange
import epicarchitect.breakbadhabits.foundation.icons.Icon
import epicarchitect.breakbadhabits.foundation.uikit.SingleSelectionGrid
import epicarchitect.breakbadhabits.foundation.uikit.button.Button
import epicarchitect.breakbadhabits.foundation.uikit.button.RequestButton
import epicarchitect.breakbadhabits.foundation.uikit.effect.ClearFocusWhenKeyboardHiddenEffect
import epicarchitect.breakbadhabits.foundation.uikit.regex.Regexps
import epicarchitect.breakbadhabits.foundation.uikit.text.Text
import epicarchitect.breakbadhabits.foundation.uikit.text.TextFieldInputAdapter
import epicarchitect.breakbadhabits.foundation.uikit.text.TextFieldValidationAdapter
import epicarchitect.breakbadhabits.foundation.uikit.text.ValidatedInputField
import epicarchitect.breakbadhabits.foundation.uikit.text.ValidatedTextField
import epicarchitect.breakbadhabits.logic.habits.validator.IncorrectHabitNewName
import epicarchitect.breakbadhabits.logic.habits.validator.IncorrectHabitTrackEventCount
import epicarchitect.breakbadhabits.logic.habits.validator.ValidatedHabitNewName
import epicarchitect.breakbadhabits.logic.habits.validator.ValidatedHabitTrackEventCount
import epicarchitect.breakbadhabits.logic.habits.validator.ValidatedHabitTrackTime

val LocalHabitCreationResourcesResources = compositionLocalOf<HabitCreationResources> {
    error("LocalHabitCreationResources not provided")
}

interface HabitCreationResources {
    val titleText: String
    val habitNameDescription: String
    val habitNameLabel: String
    val habitIconDescription: String
    val finishButtonText: String
    fun habitNameValidationError(reason: IncorrectHabitNewName.Reason): String
}
//private enum class HabitTime(
//    val titleRes: Int,
//    val offset: Duration
//) {
//    MONTH_1(R.string.habitCreation_habitTime_month_1, 30.days),
//    MONTH_3(R.string.habitCreation_habitTime_month_3, 90.days),
//    MONTH_6(R.string.habitCreation_habitTime_month_6, 180.days),
//    YEAR_1(R.string.habitCreation_habitTime_year_1, 365.days),
//    YEAR_2(R.string.habitCreation_habitTime_year_2, 365.days * 2),
//    YEAR_3(R.string.habitCreation_habitTime_year_3, 365.days * 3),
//    YEAR_4(R.string.habitCreation_habitTime_year_4, 365.days * 4),
//    YEAR_5(R.string.habitCreation_habitTime_year_5, 365.days * 5),
//    YEAR_6(R.string.habitCreation_habitTime_year_6, 365.days * 6),
//    YEAR_7(R.string.habitCreation_habitTime_year_7, 365.days * 7),
//    YEAR_8(R.string.habitCreation_habitTime_year_8, 365.days * 8),
//    YEAR_9(R.string.habitCreation_habitTime_year_9, 365.days * 9),
//    YEAR_10(R.string.habitCreation_habitTime_year_10, 365.days * 10)
//}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HabitCreation(
    habitIconSelectionController: SingleSelectionController<Icon>,
    habitNameController: ValidatedInputController<String, ValidatedHabitNewName>,
    dailyEventCountInputController: ValidatedInputController<Int, ValidatedHabitTrackEventCount>,
    firstTrackTimeInputController: ValidatedInputController<ZonedDateTimeRange, ValidatedHabitTrackTime>,
    creationController: SingleRequestController
) {
    val resources = LocalHabitCreationResourcesResources.current
    val logicModule = LocalAppModule.current.logic
//    val context = LocalContext.current
    val currentTime by logicModule.dateTime.dateTimeProvider.currentDateTimeFlow()
        .collectAsState(logicModule.dateTime.dateTimeProvider.getCurrentDateTime())

    var selectedHabitTimeIndex by rememberSaveable {
        mutableStateOf(0)
    }

//    LaunchedEffect(selectedHabitTimeIndex) {
//        val item = HabitTime.values()[selectedHabitTimeIndex]
//        val range = (currentTime - item.offset).rangeTo(currentTime)
//        firstTrackTimeInputController.changeInput(range.withZeroSeconds())
//    }

    ClearFocusWhenKeyboardHiddenEffect()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = resources.titleText,
            type = Text.Type.Title,
            priority = Text.Priority.High
        )

        Spacer(Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = resources.habitNameDescription,
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
                    else resources.habitNameValidationError(it.reason)
                }
            },
            label = resources.habitNameLabel
        )

        Spacer(Modifier.height(24.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = resources.habitIconDescription,
            type = Text.Type.Description,
            priority = Text.Priority.Medium
        )

        Spacer(Modifier.height(12.dp))

        SingleSelectionGrid(
            modifier = Modifier.padding(horizontal = 16.dp),
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
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Укажите примерно как давно у вас эта привычка:",
            type = Text.Type.Description,
            priority = Text.Priority.Medium
        )

        Spacer(Modifier.height(12.dp))

//        SingleSelectionChipRow(
//            items = HabitTime.values().map {
//                context.getString(it.titleRes)
//            },
//            onClick = {
//                selectedHabitTimeIndex = it
//            },
//            selectedIndex = selectedHabitTimeIndex,
//            edgePadding = 16.dp
//        )

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
            text = resources.finishButtonText,
            type = Button.Type.Main,
//            icon = {
//                LocalResourceIcon(resourceId = R.drawable.ic_done)
//            }
        )

        Spacer(Modifier.height(16.dp))
    }
}