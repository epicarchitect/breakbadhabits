package epicarchitect.breakbadhabits.uikit.text

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import epicarchitect.breakbadhabits.uikit.theme.AppTheme

@Stable
private val DefaultKeyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    multiline: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions? = null,
    keyboardActions: KeyboardActions? = null,
    error: String? = null,
    description: String? = null,
    readOnly: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    regex: Regex? = null
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
        modifier = modifier,
        value = when {
            regex == null -> value
            regex.matches(value) -> value
            else -> ""
        },
        onValueChange = if (regex == null) {
            onValueChange
        } else {
            { text: String ->
                if (regex.matches(text)) {
                    onValueChange(text)
                }
            }
        },
        label = if (label == null) {
            null
        } else {
            {
                Text(
                    text = label,
                    type = Text.Type.Description
                )
            }
        },
        visualTransformation = visualTransformation,
        singleLine = multiline.not(),
        maxLines = maxLines,
        keyboardOptions = finalKeyboardOptions,
        keyboardActions = finalKeyboardActions,
        isError = error != null,
        readOnly = readOnly,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        shape = MaterialTheme.shapes.small,
        supportingText = {
            AnimatedVisibility(
                visible = error != null || description != null,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                val color by animateColorAsState(
                    targetValue = if (error != null) AppTheme.colorScheme.error else LocalContentColor.current,
                    label = "support-text-color"
                )
                androidx.compose.material3.Text(
                    text = error ?: description ?: "",
                    color = color
                )
            }
        }
    )
}