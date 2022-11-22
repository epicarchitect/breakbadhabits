package epicarchitect.epicstore.compose

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable

inline fun <T> LazyListScope.epicStoreItems(
    items: List<T>,
    noinline doBeforeClear: ((key: Any?, value: Any?) -> Unit)? = null,
    noinline doAfterClear: (() -> Unit)? = null,
    noinline key: ((item: T) -> Any)? = null,
    noinline contentType: (item: T) -> Any? = { null },
    crossinline itemContent: @Composable LazyItemScope.(item: T) -> Unit
) = items(
    items = items,
    key = key,
    contentType = contentType
) { item ->
    EpicStore(
        clearWhen = {
            items.find { it == item } == null
        },
        doBeforeClear = doBeforeClear,
        doAfterClear = doAfterClear
    ) {
        itemContent(item)
    }
}