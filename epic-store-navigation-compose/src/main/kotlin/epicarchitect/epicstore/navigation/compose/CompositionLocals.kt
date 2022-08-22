package epicarchitect.epicstore.navigation.compose

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController

val LocalNavController = compositionLocalOf<NavHostController> {
    error("NavHostController not present")
}