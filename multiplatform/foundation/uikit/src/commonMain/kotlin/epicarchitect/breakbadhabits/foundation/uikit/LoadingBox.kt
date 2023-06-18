package epicarchitect.breakbadhabits.foundation.uikit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import epicarchitect.breakbadhabits.foundation.controller.LoadingController

@Composable
fun <DATA> LoadingBox(
    controller: LoadingController<DATA>,
    modifier: Modifier = Modifier,
    loaded: @Composable BoxScope.(DATA) -> Unit
) {
    val state by controller.state.collectAsState()

    Box(modifier) {
        when (val state = state) {
            is LoadingController.State.Loaded -> {
                loaded(state.data)
            }

            is LoadingController.State.Loading -> {
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    ProgressIndicator()
//                }
            }
        }
    }
}
