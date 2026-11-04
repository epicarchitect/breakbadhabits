package epicarchitect.breakbadhabits.format

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class DurationFormattingTest {

    @Test
    fun secondsStayVisibleWhenOnlyZeroSecondsRemain() {
        assertEquals(
            "34d 4h 0s",
            (34.days + 4.hours).formatted(
                accuracy = Accuracy.SECONDS,
                daysText = "d",
                hoursText = "h",
                minutesText = "m",
                secondsText = "s"
            )
        )
        assertEquals(
            "1d 0s",
            (1.days).formatted(
                accuracy = Accuracy.SECONDS,
                daysText = "d",
                hoursText = "h",
                minutesText = "m",
                secondsText = "s"
            )
        )
    }

    @Test
    fun nonZeroSecondsAreShown() {
        assertEquals(
            "4h 5m 6s",
            (4.hours + 5.minutes + 6.seconds).formatted(
                accuracy = Accuracy.SECONDS,
                daysText = "d",
                hoursText = "h",
                minutesText = "m",
                secondsText = "s"
            )
        )
    }

    @Test
    fun zeroDurationIsShownAsZeroSeconds() {
        assertEquals(
            "0s",
            (0.seconds).formatted(
                accuracy = Accuracy.SECONDS,
                daysText = "d",
                hoursText = "h",
                minutesText = "m",
                secondsText = "s"
            )
        )
    }
}
