package epicarchitect.breakbadhabits.ui.format.ios

import epicarchitect.breakbadhabits.ui.format.DurationFormatter
import kotlin.time.Duration

class IosDurationFormatter : DurationFormatter {
    override val defaultAccuracy = DurationFormatter.Accuracy.SECONDS

    override fun format(
        duration: Duration,
        accuracy: DurationFormatter.Accuracy
    ) = duration.toString()
}