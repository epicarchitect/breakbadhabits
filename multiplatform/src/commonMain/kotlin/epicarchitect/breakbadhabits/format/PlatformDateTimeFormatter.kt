package epicarchitect.breakbadhabits.format

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

interface PlatformDateTimeFormatter {
    fun format(date: LocalDate): String
    fun format(time: LocalTime): String
}