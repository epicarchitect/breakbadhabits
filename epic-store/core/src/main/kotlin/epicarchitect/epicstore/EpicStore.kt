package epicarchitect.epicstore

class EpicStore {

    private val map = mutableMapOf<Any?, Any?>()
    var isClearNeeded: () -> Boolean = { false }
    var onEntryCleared: ((key: Any?, value: Any?) -> Unit)? = null
    val onEntryClearedMap = mutableMapOf<Any?, ((Any?) -> Unit)?>()

    fun clearIfNeeded() {
        map.values.forEach {
            if (it is EpicStore) {
                it.clearIfNeeded()
            }
        }

        if (isClearNeeded()) {
            map.forEach {
                onEntryCleared?.invoke(it.key, it.value)
                onEntryClearedMap[it.key]?.invoke(it.value)
            }
            map.clear()
            onEntryClearedMap.clear()
        }
    }

    fun setOnEntryCleared(key: Any?, onCleared: ((Any?) -> Unit)?) {
        onEntryClearedMap[key] = onCleared
    }

    operator fun get(key: Any?) = map[key]

    operator fun set(key: Any?, value: Any?) = map.set(key, value)
}

inline fun <reified T> EpicStore.getOrSet(
    key: Any? = T::class, provide: () -> T
) = (get(key) ?: provide().also {
    set(key, it)
}) as T

@Suppress("UNCHECKED_CAST")
inline fun <reified T> EpicStore.setOnEntryCleared(
    key: Any?,
    noinline onCleared: ((T) -> Unit)?
) {
    setOnEntryCleared(key, onCleared as ((Any?) -> Unit)?)
}