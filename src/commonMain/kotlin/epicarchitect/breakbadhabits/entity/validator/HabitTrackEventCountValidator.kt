package epicarchitect.breakbadhabits.entity.validator

class ValidatedHabitTrackInput(
    private val input: CharSequence
): CharSequence {
    constructor(value: Int) : this(value.toString())

    fun toInt() = input.toString().toIntOrNull()

    fun incorrectReason(): IncorrectReason? {
        val intValue = toInt()
        return when {
            intValue == null || intValue <= 0 -> IncorrectReason.Empty
            else -> null
        }
    }

    override val length: Int
        get() = toString().length

    override fun get(index: Int) = toString()[index]

    override fun subSequence(startIndex: Int, endIndex: Int) = toString().subSequence(startIndex, endIndex)

    override fun toString() = toInt().toString()

    sealed class IncorrectReason {
        object Empty : IncorrectReason()
    }
}