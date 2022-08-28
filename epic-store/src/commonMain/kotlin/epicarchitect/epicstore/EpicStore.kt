package epicarchitect.epicstore

class EpicStore {

    private val map = mutableMapOf<Any?, Any?>()
    var isClearNeeded: () -> Boolean = { false }
    var doBeforeClear: (key: Any?, value: Any?) -> Unit = { _, _ -> }
    var doAfterClear: () -> Unit = { }

    fun clearIfNeeded() {
        map.values.forEach {
            if (it is EpicStore) {
                it.clearIfNeeded()
            }
        }

        if (isClearNeeded()) {
            map.forEach { doBeforeClear(it.key, it.value) }
            map.clear()
            doAfterClear()
        }
    }

    operator fun get(key: Any?) = map[key]

    operator fun set(key: Any?, value: Any?) = map.set(key, value)

}

inline fun <reified T> EpicStore.getOrSet(
    key: Any? = T::class, provide: () -> T
) = (get(key) ?: provide().also {
    set(key, it)
}) as T