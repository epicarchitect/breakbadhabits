package epicarchitect.breakbadhabits.ui

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import epicarchitect.breakbadhabits.foundation.viewmodel.ViewModel

class HolderScreenModel<T : ViewModel>(val viewModel: T) : ScreenModel {
    override fun onDispose() {
        super.onDispose()
        viewModel.clear()
    }
}

@Composable
fun <T : ViewModel> Screen.hold(
    tag: String? = null,
    factory: () -> T
): T = rememberScreenModel(tag) {
    HolderScreenModel(factory())
}.viewModel