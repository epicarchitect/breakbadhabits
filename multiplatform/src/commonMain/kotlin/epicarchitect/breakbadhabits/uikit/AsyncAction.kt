package epicarchitect.breakbadhabits.uikit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.infrastructure.AnalyticsOperator
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface AsyncAction {
    val isRunning: Boolean
    operator fun invoke()
}

@Composable
fun rememberAsyncAction(
    onError: suspend (Throwable) -> Unit = AnalyticsOperator::logError,
    action: suspend () -> Unit
): AsyncAction {
    val coroutineScope = rememberCoroutineScope()
    val currentOnError by rememberUpdatedState(onError)
    val currentAction by rememberUpdatedState(action)
    return remember {
        AsyncActionImpl(
            coroutineScope = coroutineScope,
            onError = { currentOnError(it) },
            action = { currentAction() }
        )
    }
}

@Composable
fun AsyncPrimaryButton(
    action: AsyncAction,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    PrimaryButton(
        modifier = modifier,
        text = text,
        enabled = enabled,
        loading = action.isRunning,
        onClick = action::invoke
    )
}

internal class AsyncActionImpl(
    private val coroutineScope: CoroutineScope,
    private val onError: suspend (Throwable) -> Unit,
    private val action: suspend () -> Unit
) : AsyncAction {
    override var isRunning by mutableStateOf(false)
        private set

    override fun invoke() {
        if (isRunning) return
        isRunning = true
        coroutineScope.launch {
            try {
                action()
            } catch (throwable: Throwable) {
                if (throwable is CancellationException) throw throwable
                onError(throwable)
            } finally {
                isRunning = false
            }
        }
    }
}

@Preview(widthDp = 360, heightDp = 160, name = "AsyncPrimaryButton")
@Composable
private fun AsyncPrimaryButtonPreview() {
    PreviewTheme {
        Column(Modifier.padding(16.dp)) {
            val idleAction = remember {
                object : AsyncAction {
                    override val isRunning = false
                    override fun invoke() = Unit
                }
            }
            val runningAction = remember {
                object : AsyncAction {
                    override val isRunning = true
                    override fun invoke() = Unit
                }
            }

            AsyncPrimaryButton(
                modifier = Modifier.padding(bottom = 12.dp),
                action = idleAction,
                text = "Save"
            )
            AsyncPrimaryButton(
                action = runningAction,
                text = "Save"
            )
        }
    }
}
