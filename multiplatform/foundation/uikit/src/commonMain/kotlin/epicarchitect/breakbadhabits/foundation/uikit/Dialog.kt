package epicarchitect.breakbadhabits.foundation.uikit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

@Composable
fun Dialog(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    var shown by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        delay(50)
        shown = true
    }
//
//    androidx.compose.ui.window.Dialog(
//        onCloseRequest = { onDismiss() }
//    ) {
//        Box {
//            Card {
//                content()
//
//                AnimatedVisibility(
//                    modifier = Modifier.matchParentSize(),
//                    visible = !shown,
//                    enter = fadeIn(),
//                    exit = fadeOut()
//                ) {
//                    Surface {}
//                }
//            }
//        }
//    }
}