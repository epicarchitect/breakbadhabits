package epicarchitect.breakbadhabits.math.ranges

fun <T : Comparable<T>> ClosedRange<T>.ascended() = if (isAscended) this else endInclusive..start

val <T : Comparable<T>> ClosedRange<T>.isAscended get() = start <= endInclusive

val <T : Comparable<T>> ClosedRange<T>.isStartSameAsEnd get() = start == endInclusive

fun <T : Comparable<T>> ClosedRange<T>.toList() = listOf(start, endInclusive)
fun <T : Comparable<T>> List<T>.toRange() = first()..last()

fun <T : Comparable<T>> T.asRangeOfOne() = this..this
