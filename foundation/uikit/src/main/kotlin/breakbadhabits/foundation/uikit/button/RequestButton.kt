package breakbadhabits.foundation.uikit.button

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import breakbadhabits.foundation.controller.RequestController

@Composable
fun RequestButton(
    requestController: RequestController,
    text: String,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small,
    interactionType: InteractionType = InteractionType.REGULAR,
    elevation: Dp = 1.dp,
    icon: (@Composable () -> Unit)? = null
) {
    val state by requestController.state.collectAsState()

    Button(
        onClick = requestController::request,
        text = text,
        modifier = modifier,
        shape = shape,
        enabled = state.isRequestAllowed,
        interactionType = interactionType,
        elevation = elevation,
        icon = icon
    )
}