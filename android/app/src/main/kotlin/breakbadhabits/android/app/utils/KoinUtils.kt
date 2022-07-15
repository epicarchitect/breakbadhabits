package breakbadhabits.android.app.utils

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.core.context.GlobalContext
import org.koin.core.parameter.parametersOf

@Suppress("UNCHECKED_CAST")
@Composable
inline fun <reified VM : ViewModel> composeViewModel(vararg parameters: Any?) = viewModel<VM> {
    get(parameters = parameters)
}

inline fun <reified T : Any> get(vararg parameters: Any?) = GlobalContext.get().get<T> {
    parametersOf(parameters = parameters)
}