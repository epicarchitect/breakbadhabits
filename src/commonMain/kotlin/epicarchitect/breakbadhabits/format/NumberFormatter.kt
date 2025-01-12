package epicarchitect.breakbadhabits.format

class NumberFormatter {
    /**
     * 1234567890 -> 1 234 567 890
     * */
    fun format(number: Int) = number.toString()
        .reversed()
        .chunked(3)
        .joinToString(" ")
        .reversed()
}