package epicarchitect.breakbadhabits.format

import epicarchitect.breakbadhabits.resources.AppResources

class AppFormat(
    resources: AppResources,
    platformDateTimeFormatter: PlatformDateTimeFormatter
) {
    val dateTimeFormatter = DateTimeFormatter(platformDateTimeFormatter)
    val durationFormatter = DurationFormatter(resources.strings.durationFormattingStrings)
    val monthFormatter = MonthFormatter(resources.strings.monthNames)
    val numberFormatter = NumberFormatter()
}