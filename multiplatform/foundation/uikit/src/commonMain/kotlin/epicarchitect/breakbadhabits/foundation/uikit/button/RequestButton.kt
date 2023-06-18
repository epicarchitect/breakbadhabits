package epicarchitect.breakbadhabits.foundation.uikit.button

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import epicarchitect.breakbadhabits.foundation.controller.SingleRequestController

@Composable
fun RequestButton(
    controller: SingleRequestController,
    text: String,
    modifier: Modifier = Modifier,
    type: Button.Type = Button.Type.Default,
    icon: (@Composable () -> Unit)? = null
) {
    val state by controller.state.collectAsState()

    Button(
        onClick = controller::request,
        text = text,
        modifier = modifier,
        enabled = state.isRequestAllowed,
        type = type,
        icon = icon
    )
}
