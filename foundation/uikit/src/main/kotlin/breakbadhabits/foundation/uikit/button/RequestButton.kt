package breakbadhabits.foundation.uikit.button

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import breakbadhabits.foundation.controller.RequestController
import breakbadhabits.foundation.uikit.ext.collectState

@Composable
fun RequestButton(
    requestController: RequestController,
    text: String,
    modifier: Modifier = Modifier,
    type: Button.Type = Button.Type.Default,
    icon: (@Composable () -> Unit)? = null
) {
    val state by requestController.collectState()

    Button(
        onClick = requestController::request,
        text = text,
        modifier = modifier,
        enabled = state.isRequestAllowed,
        type = type,
        icon = icon
    )
}