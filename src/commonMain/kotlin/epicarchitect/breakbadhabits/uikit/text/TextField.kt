package epicarchitect.breakbadhabits.uikit.text

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.uikit.theme.AppTheme

@Stable
private val DefaultKeyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)

@Composable
fun TextField(
    value: CharSequence,
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
            regex == null        -> value.toString()
            regex.matches(value) -> value.toString()
            else                 -> ""
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
                visible = error != null,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                val color by animateColorAsState(
                    targetValue = if (error != null) AppTheme.colorScheme.error else LocalContentColor.current,
                    label = "support-text-color"
                )
                androidx.compose.material3.Text(
                    text = error ?: "",
                    color = color
                )
            }
        }
    )
}

@Composable
fun InputCard(
    title: String,
    description: String,
    error: String? = null,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.(PaddingValues) -> Unit,
) {
    val horizontalPadding = PaddingValues(horizontal = 16.dp)
    epicarchitect.breakbadhabits.uikit.Card(
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier.padding(horizontalPadding),
            text = title,
            type = Text.Type.Title
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            modifier = Modifier.padding(horizontalPadding),
            text = description,
            type = Text.Type.Description,
            priority = Text.Priority.Low
        )
        Spacer(modifier = Modifier.height(12.dp))
        content(horizontalPadding)

        AnimatedVisibility(
            visible = error != null,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            val color by animateColorAsState(
                targetValue = if (error != null) AppTheme.colorScheme.error else LocalContentColor.current,
                label = "support-text-color"
            )
            Text(
                modifier = Modifier
                    .padding(horizontalPadding)
                    .padding(top = 8.dp),
                text = error ?: "",
                color = color,
                type = Text.Type.Description,
                priority = Text.Priority.Low
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

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
    regex: Regex? = null,
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
                .fillMaxWidth()
                .padding(it),
            value = when {
                regex == null        -> value.toString()
                regex.matches(value) -> value.toString()
                else                 -> ""
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
                        priority = Text.Priority.Low
                    )
                }
            },
            suffix = suffixText?.let {
                {
                    Text(
                        text = it,
                        priority = Text.Priority.Low
                    )
                }
            }
        )
    }
}