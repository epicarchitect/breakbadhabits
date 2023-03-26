package breakbadhabits.foundation.uikit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import breakbadhabits.foundation.controller.LoadingController
import breakbadhabits.foundation.uikit.ext.collectState

@Composable
fun <DATA> LoadingBox(
    controller: LoadingController<DATA>,
    modifier: Modifier = Modifier,
    loaded: @Composable BoxScope.(DATA) -> Unit
) {
    val state by controller.collectState()

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