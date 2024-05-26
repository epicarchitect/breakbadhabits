package epicarchitect.breakbadhabits.entity.validator

class HabitTrackEventCountInputValidation(
    private val input: CharSequence
) {
    constructor(value: Int) : this(value.toString())

    fun toInt() = input.toString().toIntOrNull()

    fun incorrectReason(): IncorrectReason? {
        val intValue = toInt()
        return when {
            intValue == null || intValue <= 0 -> IncorrectReason.Empty
            else                              -> null
        }
    }

    override fun toString() = toInt().toString()

    sealed class IncorrectReason {
        object Empty : IncorrectReason()
    }
}