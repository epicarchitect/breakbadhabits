package epicarchitect.breakbadhabits.foundation.uikit.text

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.foundation.controller.InputController
import epicarchitect.breakbadhabits.foundation.controller.ValidatedInputController
import epicarchitect.breakbadhabits.foundation.uikit.ext.onFocusLost

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

@Composable
fun <VALIDATION_RESULT> ValidatedTextField(
    controller: ValidatedInputController<String, VALIDATION_RESULT>,
    validationAdapter: TextFieldValidationAdapter<VALIDATION_RESULT>,
    modifier: Modifier = Modifier,
    label: String? = null,
    multiline: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions? = null,
    keyboardActions: KeyboardActions? = null,
    readOnly: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    regex: Regex? = null
) {
    ValidatedInputField(
        modifier = modifier,
        controller = controller,
        inputAdapter = TextInputAdapter,
        validationAdapter = validationAdapter,
        label = label,
        multiline = multiline,
        maxLines = maxLines,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        readOnly = readOnly,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        regex = regex
    )
}

@Composable
fun <INPUT, VALIDATION_RESULT> ValidatedInputField(
    controller: ValidatedInputController<INPUT, VALIDATION_RESULT>,
    inputAdapter: TextFieldInputAdapter<INPUT>,
    validationAdapter: TextFieldValidationAdapter<VALIDATION_RESULT>,
    modifier: Modifier = Modifier,
    label: String? = null,
    multiline: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions? = null,
    keyboardActions: KeyboardActions? = null,
    readOnly: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    regex: Regex? = null
) {
    val state by controller.state.collectAsState()
    val error = validationAdapter.extractErrorMessage(state.validationResult)

    Column(modifier) {
        InputField(
            modifier = Modifier.onFocusLost(controller::validate),
            controller = controller,
            inputAdapter = inputAdapter,
            label = label,
            multiline = multiline,
            maxLines = maxLines,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            readOnly = readOnly,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = error != null,
            regex = regex
        )

        AnimatedVisibility(
            visible = error != null,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            ErrorText(
                modifier = Modifier.padding(top = 8.dp),
                text = error ?: ""
            )
        }
    }
}

@Composable
fun TextField(
    controller: InputController<String>,
    modifier: Modifier = Modifier,
    label: String? = null,
    multiline: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions? = null,
    keyboardActions: KeyboardActions? = null,
    readOnly: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    regex: Regex? = null,
    isError: Boolean = false
) {
    InputField(
        modifier = modifier,
        controller = controller,
        inputAdapter = TextInputAdapter,
        label = label,
        multiline = multiline,
        maxLines = maxLines,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        readOnly = readOnly,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = isError,
        regex = regex
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun <INPUT> InputField(
    controller: InputController<INPUT>,
    inputAdapter: TextFieldInputAdapter<INPUT>,
    modifier: Modifier = Modifier,
    label: String? = null,
    multiline: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions? = null,
    keyboardActions: KeyboardActions? = null,
    readOnly: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    regex: Regex? = null,
    isError: Boolean = false
) {
    val state by controller.state.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val finalKeyboardOptions = keyboardOptions ?: remember {
        KeyboardOptions(imeAction = ImeAction.Done)
    }
    val finalKeyboardActions = keyboardActions ?: remember {
        KeyboardActions(
            onDone = {
                keyboardController?.hide()
            }
        )
    }

    TextField(
        modifier = modifier.fillMaxWidth(),
        value = inputAdapter.decodeInput(state.input) ?: "",
        onValueChange = {
            controller.changeInput(inputAdapter.encodeInput(it))
        },
        label = label,
        multiline = multiline,
        maxLines = maxLines,
        visualTransformation = visualTransformation,
        keyboardOptions = finalKeyboardOptions,
        keyboardActions = finalKeyboardActions,
        readOnly = readOnly,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = isError,
        regex = regex
    )
}