package breakbadhabits.foundation.uikit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import breakbadhabits.foundation.controller.DataFlowController

@Composable
fun <DATA> DataFlowBox(
    controller: DataFlowController<DATA>,
    modifier: Modifier = Modifier,
    loaded: @Composable BoxScope.(DATA) -> Unit
) {
    val state by controller.state.collectAsState()

    Box(modifier) {
        when (val state = state) {
            is DataFlowController.State.Loaded -> {
                loaded(state.data)
            }
            is DataFlowController.State.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ProgressIndicator()
                }
            }
        }
    }
}