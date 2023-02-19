package breakbadhabits.framework.uikit.text

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    multiline: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isError: Boolean = false,
    readOnly: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    regex: Regex? = null,
) {
    OutlinedTextField(
        modifier = modifier,
        value = when {
            regex == null -> value
            regex.matches(value) -> value
            else -> ""
        },
        onValueChange = if (regex == null) onValueChange
        else { text: String ->
            if (regex.matches(text)) {
                onValueChange(text)
            }
        },
        label = if (label == null) null else {
            {
                Text(text = label)
            }
        },
        visualTransformation = visualTransformation,
        singleLine = multiline.not(),
        maxLines = maxLines,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        isError = isError,
        readOnly = readOnly,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon
    )
}