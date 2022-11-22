package epicarchitect.epicstore.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import epicarchitect.epicstore.EpicStore
import epicarchitect.epicstore.getOrSet
import java.util.*

@Composable
fun RootEpicStore(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalEpicStore provides RootEpicStoreHolder.instance,
        content = content
    )
}

@Composable
fun EpicStore(
    key: Any = rememberSaveable(init = UUID::randomUUID),
    clearWhen: () -> Boolean = { false },
    doBeforeClear: ((key: Any?, value: Any?) -> Unit)? = null,
    doAfterClear: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val epicStore = LocalEpicStore.current
    LaunchedEffect(clearWhen()) {
        epicStore.clearIfNeeded()
    }

    CompositionLocalProvider(
        LocalEpicStore provides rememberEpicStoreEntry(key, entry = ::EpicStore).apply {
            this.isClearNeeded = clearWhen
            this.doBeforeClear = doBeforeClear
            this.doAfterClear = doAfterClear
        },
        content = content
    )
}

@Composable
inline fun <reified T> rememberEpicStoreEntry(
    key: Any = rememberSaveable(init = UUID::randomUUID),
    noinline doBeforeClear: ((T) -> Unit)? = null,
    noinline doAfterClear: (() -> Unit)? = null,
    noinline entry: @DisallowComposableCalls () -> T,
): T {
    val epicStore = LocalEpicStore.current
    return remember(key) { epicStore.getOrSet(key, entry) }
}