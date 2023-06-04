package epicarchitect.breakbadhabits.ui

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import epicarchitect.breakbadhabits.foundation.viewmodel.ViewModel

class HolderViewModel<T : ViewModel>(val viewModel: T) : ScreenModel {
    override fun onDispose() {
        super.onDispose()
        println("test123 clear!!!")
        viewModel.clear()
    }
}

@Composable
fun <T : ViewModel> Screen.viewModel(
    tag: String? = null,
    factory: () -> T
): T = rememberScreenModel(tag) {
    HolderViewModel(factory())
}.viewModel