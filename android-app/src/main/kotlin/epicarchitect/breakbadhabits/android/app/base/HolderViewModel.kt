package epicarchitect.breakbadhabits.android.app.base

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import epicarchitect.breakbadhabits.foundation.viewmodel.ViewModel as FoundationViewModel

class HolderViewModel<T : FoundationViewModel>(val viewModel: T) : ViewModel() {
    override fun onCleared() {
        super.onCleared()
        viewModel.clear()
    }
}

@Composable
fun <T : FoundationViewModel> holderViewModel(
    key: String? = null,
    factory: () -> T
): T = viewModel(key = key) {
    HolderViewModel(factory())
}.viewModel