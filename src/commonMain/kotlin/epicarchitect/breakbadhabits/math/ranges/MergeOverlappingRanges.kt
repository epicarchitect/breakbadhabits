package epicarchitect.breakbadhabits.math.ranges

/**
 * [1..3, 3..6, 8..10, 7..9] -> [1..6, 7..10]
 * */
fun <T : Comparable<T>> Iterable<ClosedRange<T>>.mergedByOverlappingRanges(): List<ClosedRange<T>> {
    val sortedRanges = map(ClosedRange<T>::ascended).sortedBy(ClosedRange<T>::start)
    if (sortedRanges.isEmpty()) return emptyList()

    val result = mutableListOf<ClosedRange<T>>()
    var currentRange = sortedRanges[0]

    for (i in 1 until sortedRanges.size) {
        val nextRange = sortedRanges[i]
        currentRange = if (nextRange.start <= currentRange.endInclusive) {
            currentRange.start..maxOf(currentRange.endInclusive, nextRange.endInclusive)
        } else {
            result.add(currentRange)
            nextRange
        }
    }

    result.add(currentRange)
    return result
}