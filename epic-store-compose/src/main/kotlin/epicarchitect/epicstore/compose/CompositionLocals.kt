package epicarchitect.epicstore.compose

import androidx.compose.runtime.compositionLocalOf
import epicarchitect.epicstore.EpicStore

val LocalEpicStore = compositionLocalOf<EpicStore> {
    error("EpicStore not present")
}