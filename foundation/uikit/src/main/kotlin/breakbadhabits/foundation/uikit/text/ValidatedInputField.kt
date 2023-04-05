package breakbadhabits.foundation.uikit.text

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import breakbadhabits.foundation.controller.ValidatedInputController
import breakbadhabits.foundation.uikit.ext.collectState
import breakbadhabits.foundation.uikit.ext.onFocusLost

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
    regex: Regex? = null,
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

@OptIn(ExperimentalComposeUiApi::class)
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
    regex: Regex? = null,
) {
    val state by controller.collectState()
    val error = validationAdapter.extractErrorMessage(state.validationResult)
    val keyboardController = LocalSoftwareKeyboardController.current

    LocalDensity
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

    Column(modifier) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusLost(controller::validate),
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
            isError = error != null,
            regex = regex
        )

        AnimatedVisibility(visible = error != null) {
            ErrorText(
                modifier = Modifier.padding(top = 8.dp),
                text = error ?: ""
            )
        }
    }
}