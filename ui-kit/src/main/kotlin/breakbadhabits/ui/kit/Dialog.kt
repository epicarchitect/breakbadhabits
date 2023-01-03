package breakbadhabits.ui.kit

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

@Composable
fun Dialog(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    var show by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        delay(50)
        show = true
    }

    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismiss
    ) {
        Box {
            Card {
                content()

                AnimatedVisibility(
                    modifier = Modifier.matchParentSize(),
                    visible = !show,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Surface {}
                }
            }
        }
    }
}