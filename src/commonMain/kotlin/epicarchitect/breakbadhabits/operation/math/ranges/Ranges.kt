package epicarchitect.breakbadhabits.operation.math.ranges

fun <T : Comparable<T>> ClosedRange<T>.ascended() = if (isAscended) this else endInclusive..start

val <T : Comparable<T>> ClosedRange<T>.isAscended get() = start <= endInclusive
