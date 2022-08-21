package epicarchitect.epicstore.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import epicarchitect.epicstore.EpicStore
import epicarchitect.epicstore.getOrSet

@Composable
fun RootEpicStore(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalEpicStore provides RootEpicStoreHolder.instance,
        content = content
    )
}

@Composable
fun EpicStore(
    key: Any? = null,
    isClearNeeded: () -> Boolean = { false },
    doBeforeClear: (key: Any?, value: Any?) -> Unit = { _, _ -> },
    doAfterClear: () -> Unit = { },
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalEpicStore provides LocalEpicStore.current.getOrSet(key) {
            EpicStore(
                isClearNeeded = isClearNeeded,
                doBeforeClear = doBeforeClear,
                doAfterClear = doAfterClear
            )
        },
        content = content
    )
}

