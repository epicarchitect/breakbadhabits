package epicarchitect.breakbadhabits.foundation.uikit.text

interface TextFieldInputAdapter<INPUT> {
    fun decodeInput(input: INPUT): String?
    fun encodeInput(input: String): INPUT
}

interface TextFieldValidationAdapter<VALIDATION_RESULT> {
    fun extractErrorMessage(result: VALIDATION_RESULT?): String?
}

fun <INPUT> TextFieldInputAdapter(
    decodeInput: (input: INPUT) -> String?,
    encodeInput: (input: String) -> INPUT
) = object : TextFieldInputAdapter<INPUT> {
    override fun decodeInput(input: INPUT) = decodeInput(input)

    override fun encodeInput(input: String) = encodeInput(input)
}

fun <VALIDATION_RESULT> TextFieldValidationAdapter(
    extractErrorMessage: (result: VALIDATION_RESULT?) -> String?
) = object : TextFieldValidationAdapter<VALIDATION_RESULT> {
    override fun extractErrorMessage(result: VALIDATION_RESULT?) = extractErrorMessage(result)
}

private object TextInputAdapter : TextFieldInputAdapter<String> {
    override fun decodeInput(input: String) = input
    override fun encodeInput(input: String) = input
}
//
//@Composable
//fun <VALIDATION_RESULT> ValidatedTextField(
//    controller: ValidatedInputController<String, VALIDATION_RESULT>,
//    validationAdapter: TextFieldValidationAdapter<VALIDATION_RESULT>,
//    modifier: Modifier = Modifier,
//    label: String? = null,
//    multiline: Boolean = false,
//    maxLines: Int = Int.MAX_VALUE,
//    visualTransformation: VisualTransformation = VisualTransformation.None,
//    keyboardOptions: KeyboardOptions? = null,
//    keyboardActions: KeyboardActions? = null,
//    readOnly: Boolean = false,
//    leadingIcon: @Composable (() -> Unit)? = null,
//    trailingIcon: @Composable (() -> Unit)? = null,
//    regex: Regex? = null
//) {
//    ValidatedInputField(
//        modifier = modifier,
//        controller = controller,
//        inputAdapter = TextInputAdapter,
//        validationAdapter = validationAdapter,
//        label = label,
//        multiline = multiline,
//        maxLines = maxLines,
//        visualTransformation = visualTransformation,
//        keyboardOptions = keyboardOptions,
//        keyboardActions = keyboardActions,
//        readOnly = readOnly,
//        leadingIcon = leadingIcon,
//        trailingIcon = trailingIcon,
//        regex = regex
//    )
//}
//
//@Composable
//fun <INPUT, VALIDATION_RESULT> ValidatedInputField(
//    controller: ValidatedInputController<INPUT, VALIDATION_RESULT>,
//    inputAdapter: TextFieldInputAdapter<INPUT>,
//    validationAdapter: TextFieldValidationAdapter<VALIDATION_RESULT>,
//    modifier: Modifier = Modifier,
//    label: String? = null,
//    multiline: Boolean = false,
//    maxLines: Int = Int.MAX_VALUE,
//    visualTransformation: VisualTransformation = VisualTransformation.None,
//    keyboardOptions: KeyboardOptions? = null,
//    keyboardActions: KeyboardActions? = null,
//    readOnly: Boolean = false,
//    leadingIcon: @Composable (() -> Unit)? = null,
//    trailingIcon: @Composable (() -> Unit)? = null,
//    regex: Regex? = null
//) {
//    val state by controller.state.collectAsState()
//    val error = validationAdapter.extractErrorMessage(state.validationResult)
//
//    Column(modifier) {
//        InputField(
//            modifier = Modifier.onFocusLost(controller::validate),
//            controller = controller,
//            inputAdapter = inputAdapter,
//            label = label,
//            multiline = multiline,
//            maxLines = maxLines,
//            visualTransformation = visualTransformation,
//            keyboardOptions = keyboardOptions,
//            keyboardActions = keyboardActions,
//            readOnly = readOnly,
//            leadingIcon = leadingIcon,
//            trailingIcon = trailingIcon,
//            isError = error != null,
//            regex = regex
//        )
//
//        AnimatedVisibility(
//            visible = error != null,
//            enter = fadeIn() + expandVertically(),
//            exit = fadeOut() + shrinkVertically()
//        ) {
//            ErrorText(
//                modifier = Modifier.padding(top = 8.dp),
//                text = error ?: ""
//            )
//        }
//    }
//}
//
//@Composable
//fun TextField(
//    controller: InputController<String>,
//    modifier: Modifier = Modifier,
//    label: String? = null,
//    multiline: Boolean = false,
//    maxLines: Int = Int.MAX_VALUE,
//    visualTransformation: VisualTransformation = VisualTransformation.None,
//    keyboardOptions: KeyboardOptions? = null,
//    keyboardActions: KeyboardActions? = null,
//    readOnly: Boolean = false,
//    leadingIcon: @Composable (() -> Unit)? = null,
//    trailingIcon: @Composable (() -> Unit)? = null,
//    regex: Regex? = null,
//    isError: Boolean = false
//) {
//    InputField(
//        modifier = modifier,
//        controller = controller,
//        inputAdapter = TextInputAdapter,
//        label = label,
//        multiline = multiline,
//        maxLines = maxLines,
//        visualTransformation = visualTransformation,
//        keyboardOptions = keyboardOptions,
//        keyboardActions = keyboardActions,
//        readOnly = readOnly,
//        leadingIcon = leadingIcon,
//        trailingIcon = trailingIcon,
//        isError = isError,
//        regex = regex
//    )
//}
//
//@OptIn(ExperimentalComposeUiApi::class)
//@Composable
//fun <INPUT> InputField(
//    controller: InputController<INPUT>,
//    inputAdapter: TextFieldInputAdapter<INPUT>,
//    modifier: Modifier = Modifier,
//    label: String? = null,
//    multiline: Boolean = false,
//    maxLines: Int = Int.MAX_VALUE,
//    visualTransformation: VisualTransformation = VisualTransformation.None,
//    keyboardOptions: KeyboardOptions? = null,
//    keyboardActions: KeyboardActions? = null,
//    readOnly: Boolean = false,
//    leadingIcon: @Composable (() -> Unit)? = null,
//    trailingIcon: @Composable (() -> Unit)? = null,
//    regex: Regex? = null,
//    isError: Boolean = false
//) {
//    val state by controller.state.collectAsState()
//
//    TextField(
//        modifier = modifier.fillMaxWidth(),
//        value = inputAdapter.decodeInput(state.input) ?: "",
//        onValueChange = {
//            controller.changeInput(inputAdapter.encodeInput(it))
//        },
//        label = label,
//        multiline = multiline,
//        maxLines = maxLines,
//        visualTransformation = visualTransformation,
//        keyboardOptions = keyboardOptions,
//        keyboardActions = keyboardActions,
//        readOnly = readOnly,
//        leadingIcon = leadingIcon,
//        trailingIcon = trailingIcon,
//        regex = regex
//    )
//}