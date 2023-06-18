package epicarchitect.breakbadhabits.di.declaration.ui

import epicarchitect.breakbadhabits.ui.format.DateTimeFormatter
import epicarchitect.breakbadhabits.ui.format.DurationFormatter

interface FormatModule {
    val dateTimeFormatter: DateTimeFormatter
    val durationFormatter: DurationFormatter
}
