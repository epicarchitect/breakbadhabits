package epicarchitect.epicstore.compose

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import epicarchitect.epicstore.EpicStore
import epicarchitect.epicstore.getOrSet
import java.util.UUID

@Composable
fun RootEpicStore(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalEpicStore provides RootEpicStoreHolder.instance,
        content = content
    )
}

@Composable
fun EpicStore(
    key: Any = rememberSaveable { UUID.randomUUID() },
    clearWhen: () -> Boolean = { false },
    doBeforeClear: (key: Any?, value: Any?) -> Unit = { _, _ -> },
    doAfterClear: () -> Unit = { },
    autoDestroy: Boolean = true,
    content: @Composable () -> Unit
) {
    Log.d("testasd", "key $key")
    if (autoDestroy) {
        val epicStore = LocalEpicStore.current
        LaunchedEffect(clearWhen()) {
            epicStore.clearIfNeeded()
        }
    }

    CompositionLocalProvider(
        LocalEpicStore provides rememberEpicStoreEntry(key) {
            EpicStore(
                isClearNeeded = clearWhen,
                doBeforeClear = doBeforeClear,
                doAfterClear = doAfterClear
            )
        },
        content = content
    )
}

@Composable
inline fun <reified T> rememberEpicStoreEntry(
    key: Any = rememberSaveable(init = UUID::randomUUID),
    noinline entry: @DisallowComposableCalls () -> T,
): T {
    val epicStore = LocalEpicStore.current
    return remember { epicStore.getOrSet(key, entry) }
}