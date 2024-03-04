package epicarchitect.breakbadhabits

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.lifecycleScope
import epicarchitect.breakbadhabits.presentation.app.AppViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : ComponentActivity() {
    private val onBackPressCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            appViewModel.navigation.remove(appViewModel.navigation.last())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, onBackPressCallback)
        appViewModel.navigation.onEach {
            onBackPressCallback.isEnabled = it.size > 1
        }.launchIn(lifecycleScope)
        setContent {
            App(appViewModel)
        }
    }
}