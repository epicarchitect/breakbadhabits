package breakbadhabits.android.app

import android.util.Log
import androidx.compose.runtime.Composable

fun Any.log(message: String) {
    Log.d("test123", "Message from $this")
    Log.d("test123", message)
}