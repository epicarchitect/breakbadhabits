package epicarchitect.breakbadhabits.datetime.format

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import platform.Foundation.NSDateFormatter

actual fun Month.formatted(): String = NSDateFormatter().standaloneMonthSymbols[ordinal].toString()
actual fun LocalDate.formatted(withYear: Boolean): String = this.toString()
actual fun LocalTime.formatted(): String = this.toString()