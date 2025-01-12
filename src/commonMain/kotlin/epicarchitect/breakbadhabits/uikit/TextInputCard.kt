package epicarchitect.breakbadhabits.uikit

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation

@Stable
private val DefaultKeyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)


@Composable
fun TextInputCard(
    title: String,
    description: String,
    value: CharSequence,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    multiline: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions? = null,
    keyboardActions: KeyboardActions? = null,
    error: String? = null,
    readOnly: Boolean = false,
    prefixText: String? = null,
    suffixText: String? = null,
) {
    InputCard(
        modifier = modifier,
        title = title,
        description = description,
        error = error
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current

        val finalKeyboardOptions = keyboardOptions ?: DefaultKeyboardOptions
        val finalKeyboardActions = keyboardActions ?: remember {
            KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            )
        }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = value.toString(),
            onValueChange = onValueChange,
            visualTransformation = visualTransformation,
            singleLine = multiline.not(),
            maxLines = maxLines,
            keyboardOptions = finalKeyboardOptions,
            keyboardActions = finalKeyboardActions,
            isError = error != null,
            readOnly = readOnly,
            shape = MaterialTheme.shapes.small,
            prefix = prefixText?.let {
                {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            },
            suffix = suffixText?.let {
                {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        )
    }
}