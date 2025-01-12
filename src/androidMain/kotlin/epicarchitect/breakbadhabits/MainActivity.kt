package epicarchitect.breakbadhabits

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.MainThread
import epicarchitect.breakbadhabits.screens.root.RootScreen
import kotlinx.coroutines.Dispatchers

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RootScreen(
                environment = appEnvironment
            )
        }
    }
}