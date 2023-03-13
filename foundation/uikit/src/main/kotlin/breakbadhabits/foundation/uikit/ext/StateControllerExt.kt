package breakbadhabits.foundation.uikit.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import breakbadhabits.foundation.controller.StateController

@Composable
fun <T> StateController<T>.collectState() = state.collectAsState()