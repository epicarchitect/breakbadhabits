package breakbadhabits.foundation.math.ranges

fun <T : Comparable<T>> ClosedRange<T>.ascended() = if (isAscended) this else endInclusive..start

val <T : Comparable<T>> ClosedRange<T>.isAscended get() = start <= endInclusive

fun <T : Comparable<T>> ClosedRange<T>.valuesAreEquals(
    range: ClosedRange<T>
) = start == range.start && endInclusive == range.endInclusive

operator fun <T : Comparable<T>> ClosedRange<T>.contains(
    range: ClosedRange<T>
): Boolean = start in range || endInclusive in range

fun <T : Comparable<T>> combineOrNull(
    range1: ClosedRange<T>,
    range2: ClosedRange<T>,
): ClosedRange<T>? {
    val r1 = range1.ascended()
    val r2 = range2.ascended()
    return when {
        r1.start == r2.start && r1.endInclusive == r2.endInclusive -> r1
        r1 in r2 || r2 in r1 -> listOf(
            r1.start,
            r1.endInclusive,
            r2.start,
            r2.endInclusive
        ).let {
            it.min()..it.max()
        }
        else -> null
    }
}