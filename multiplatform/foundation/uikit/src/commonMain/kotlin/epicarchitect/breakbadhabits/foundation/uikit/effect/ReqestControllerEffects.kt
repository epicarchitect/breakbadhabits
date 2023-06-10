package epicarchitect.breakbadhabits.foundation.uikit.effect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import epicarchitect.breakbadhabits.foundation.controller.SingleRequestController


@Composable
inline fun <reified T : SingleRequestController.RequestState> LaunchedEffectWhenRequest(
    controller: SingleRequestController,
    crossinline action: suspend () -> Unit
) {
    val requestState = controller.state.collectAsState().value.requestState
    LaunchedEffect(requestState) {
        if (requestState is T) {
            action()
        }
    }
}

@Composable
fun LaunchedEffectWhenExecuted(
    controller: SingleRequestController,
    action: suspend () -> Unit
) {
    LaunchedEffectWhenRequest<SingleRequestController.RequestState.Executed>(
        controller = controller,
        action = action
    )
}