package epicarchitect.breakbadhabits.features.habits.tracks.creation

import androidx.compose.runtime.Composable
import epicarchitect.breakbadhabits.features.LocalAppModule


@Composable
fun HabitTrackCreation(dependencies: HabitTrackCreationDependencies) {
    val appModule = LocalAppModule.current
    val dateTimeFormatter = appModule.format.dateTimeFormatter
//    val uiModule = LocalAppModule.current
//    val currentTime by logicModule.dateTime.dateTimeProvider.currentDateTimeFlow()
//        .collectAsState(logicModule.dateTime.dateTimeProvider.getCurrentDateTime())
//    var rangeSelectionShow by remember { mutableStateOf(false) }
//    val rangeState by viewModel.timeInputController.state.collectAsState()
//    var selectedTimeSelectionIndex by remember { mutableStateOf(0) }
//    val resources = LocalHabitTrackCreationResources.current
//
//    ClearFocusWhenKeyboardHiddenEffect()
//
//    if (rangeSelectionShow) {
//        val state = rememberEpicDatePickerState(
//            selectedDates = viewModel.timeInputController.state.value.input.let {
//                listOf(it.start.date, it.endInclusive.date)
//            },
//            selectionMode = EpicDatePickerState.SelectionMode.Range
//        )
//        Dialog(
//            onDismiss = {
//                rangeSelectionShow = false
//                viewModel.timeInputController.changeInput(
//                    state.selectedDates.let {
//                        LocalDateTime(
//                            it.first(),
//                            LocalTime(0, 0)
//                        )..LocalDateTime(
//                            it.last(),
//                            LocalTime(0, 0)
//                        )
//                    }
//                )
//            }
//        ) {
//            EpicDatePicker(
//                modifier = Modifier,
//                state = state
//            )
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(rememberScrollState())
//    ) {
//        Spacer(Modifier.height(16.dp))
//
//        Text(
//            modifier = Modifier.padding(horizontal = 16.dp),
//            text = resources.titleText,
//            type = Text.Type.Title,
//            priority = Text.Priority.High
//        )
//
//        Spacer(Modifier.height(4.dp))
//
//        LoadingBox(viewModel.habitController) {
//            if (it != null) {
//                Text(
//                    modifier = Modifier.padding(horizontal = 16.dp),
//                    text = resources.habitNameLabel(it.name),
//                    type = Text.Type.Description,
//                    priority = Text.Priority.Low
//                )
//            }
//        }
//
//        Spacer(Modifier.height(16.dp))
//
//        Text(
//            modifier = Modifier.padding(horizontal = 16.dp),
//            text = "Укажите сколько примерно было событий привычки каждый день"
//        )
//
//        Spacer(Modifier.height(12.dp))
//
//        ValidatedInputField(
//            modifier = Modifier.padding(horizontal = 16.dp),
//            controller = viewModel.eventCountInputController,
//            inputAdapter = remember {
//                TextFieldInputAdapter(
//                    decodeInput = { it.toString() },
//                    encodeInput = {
//                        it.toIntOrNull() ?: 0
//                    }
//                )
//            },
//            validationAdapter = remember {
//                TextFieldValidationAdapter {
//                    if (it !is IncorrectHabitTrackEventCount) {
//                        null
//                    } else {
//                        when (it.reason) {
//                            is IncorrectHabitTrackEventCount.Reason.Empty -> {
//                                "Поле не может быть пустым"
//                            }
//                        }
//                    }
//                }
//            },
//            label = "Число событий в день",
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Number
//            ),
//            regex = Regexps.integersOrEmpty(maxCharCount = 4)
//        )
//
//        Spacer(Modifier.height(24.dp))
//
//        Text(
//            modifier = Modifier.padding(horizontal = 16.dp),
//            text = "Укажите когда произошло событие:"
//        )
//
//        Spacer(Modifier.height(12.dp))
//
//        LaunchedEffect(selectedTimeSelectionIndex) {
//            if (selectedTimeSelectionIndex == 0) {
//                viewModel.timeInputController.changeInput(
//                    currentTime..currentTime
//                )
//            }
//
//            if (selectedTimeSelectionIndex == 1) {
////                timeInputController.changeInput(
////                    ZonedDateTimeRange.of(
////                        currentTime.minus(1.days).withZeroSeconds()
////                    )
////                )
//            }
//        }
//
//        SingleSelectionChipRow(
//            items = listOf("Сейчас", "Вчера", "Свой интервал"),
//            onClick = {
//                if (it == 2) {
//                    rangeSelectionShow = true
//                }
//                selectedTimeSelectionIndex = it
//            },
//            selectedIndex = selectedTimeSelectionIndex
//        )
//
//        Spacer(Modifier.height(12.dp))
//
//        Button(
//            modifier = Modifier.padding(horizontal = 16.dp),
//            onClick = {
//                rangeSelectionShow = true
//            },
//            text = rangeState.input.let {
//                if (it.isStartSameAsEnd) {
//                    val start = dateTimeFormatter.formatDateTime(it.start)
//                    "Дата и время: $start"
//                } else {
//                    val start = dateTimeFormatter.formatDateTime(it.start)
//                    val end = dateTimeFormatter.formatDateTime(it.endInclusive)
//                    "Первое событие: $start, последнее событие: $end"
//                }
//            }
//        )
//
//        (rangeState.validationResult as? IncorrectHabitTrackDateTimeRange)?.let {
//            Spacer(Modifier.height(8.dp))
//            when (it.reason) {
//                IncorrectHabitTrackDateTimeRange.Reason.BiggestThenCurrentTime -> {
//                    ErrorText(
//                        modifier = Modifier.padding(horizontal = 16.dp),
//                        text = "Нельзя выбрать время больше чем текущее"
//                    )
//                }
//            }
//        }
//
//        Spacer(Modifier.height(24.dp))
//
//        Text(
//            modifier = Modifier.padding(horizontal = 16.dp),
//            text = resources.commentDescription
//        )
//
//        Spacer(Modifier.height(12.dp))
//
//        ValidatedTextField(
//            modifier = Modifier.padding(horizontal = 16.dp),
//            label = resources.commentLabel,
//            controller = viewModel.commentInputController,
//            validationAdapter = remember {
//                TextFieldValidationAdapter {
//                    null
//                }
//            }
//        )
//
//        Spacer(modifier = Modifier.weight(1.0f))
//
//        Spacer(modifier = Modifier.height(48.dp))
//
//        Text(
//            modifier = Modifier
//                .padding(horizontal = 16.dp)
//                .align(Alignment.End),
//            text = resources.finishDescription
//        )
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        RequestButton(
//            modifier = Modifier
//                .padding(horizontal = 16.dp)
//                .align(Alignment.End),
//            controller = viewModel.creationController,
//            text = resources.finishButton,
//            type = Button.Type.Main,
//            icon = {
//                Icon(Icons.Done)
//            }
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//    }
}