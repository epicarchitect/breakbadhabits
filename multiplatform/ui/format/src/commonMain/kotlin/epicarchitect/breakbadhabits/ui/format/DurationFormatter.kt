package epicarchitect.breakbadhabits.ui.format

import kotlin.time.Duration

interface DurationFormatter {
    val defaultAccuracy: Accuracy

    fun format(
        duration: Duration,
        accuracy: Accuracy = defaultAccuracy
    ): String

    enum class Accuracy(val order: Int) {
        DAYS(1),
        HOURS(2),
        MINUTES(3),
        SECONDS(4)
    }
}