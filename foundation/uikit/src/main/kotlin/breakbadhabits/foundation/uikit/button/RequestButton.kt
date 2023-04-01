package breakbadhabits.foundation.uikit.button

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import breakbadhabits.foundation.controller.SingleRequestController
import breakbadhabits.foundation.uikit.ext.collectState

@Composable
fun RequestButton(
    controller: SingleRequestController,
    text: String,
    modifier: Modifier = Modifier,
    type: Button.Type = Button.Type.Default,
    icon: (@Composable () -> Unit)? = null
) {
    val state by controller.collectState()

    Button(
        onClick = controller::request,
        text = text,
        modifier = modifier,
        enabled = state.isRequestAllowed,
        type = type,
        icon = icon
    )
}