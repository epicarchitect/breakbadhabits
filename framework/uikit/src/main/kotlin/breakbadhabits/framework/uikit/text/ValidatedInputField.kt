package breakbadhabits.framework.uikit.text

import androidx.compose.animation.AnimatedVisibility
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
import breakbadhabits.framework.controller.ValidatedInputController
import breakbadhabits.framework.uikit.ext.onFocusLost

interface TextFieldAdapter<INPUT, VALIDATION_RESULT> {
    fun decodeInput(input: INPUT): String
    fun encodeInput(input: String): INPUT
    fun extractErrorMessage(result: VALIDATION_RESULT?): String?
}

fun <INPUT, VALIDATION_RESULT> TextFieldAdapter(
    decodeInput: (input: INPUT) -> String,
    encodeInput: (input: String) -> INPUT,
    extractErrorMessage: (result: VALIDATION_RESULT?) -> String?
) = object : TextFieldAdapter<INPUT, VALIDATION_RESULT> {
    override fun decodeInput(input: INPUT) = decodeInput(input)

    override fun encodeInput(input: String) = encodeInput(input)

    override fun extractErrorMessage(result: VALIDATION_RESULT?) = extractErrorMessage(result)

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun <INPUT, VALIDATION_RESULT> ValidatedInputField(
    controller: ValidatedInputController<INPUT, VALIDATION_RESULT>,
    adapter: TextFieldAdapter<INPUT, VALIDATION_RESULT>,
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
    val state by controller.state.collectAsState()
    val error = adapter.extractErrorMessage(state.validationResult)
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

    Column(modifier) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusLost(controller::validate),
            value = adapter.decodeInput(state.input),
            onValueChange = {
                controller.changeInput(adapter.encodeInput(it))
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