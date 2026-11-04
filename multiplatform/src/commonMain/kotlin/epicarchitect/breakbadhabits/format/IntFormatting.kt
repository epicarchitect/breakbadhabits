package epicarchitect.breakbadhabits.format

fun Int.formatted(): String = toString()
    .reversed()
    .chunked(3)
    .joinToString(" ")
    .reversed()
