package epicarchitect.breakbadhabits.settings

internal fun <T> List<T>.orderedBy(
    habitOrder: List<String>,
    id: (T) -> String
): List<T> {
    val positions = habitOrder
        .mapIndexed { index, id -> id to index }
        .toMap()
    return sortedBy { positions[id(it)] ?: Int.MAX_VALUE }
}
