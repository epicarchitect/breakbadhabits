package epicarchitect.breakbadhabits.datetime.format

import android.content.Context
import android.text.format.DateFormat
import epicarchitect.breakbadhabits.format.DateTimeFormatter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalTime
import java.util.Locale
import java.time.format.DateTimeFormatter as JavaDateTimeFormatter

private const val DATE_PATTERN = "d MMMM yyyy"
private const val TIME_PATTERN_24 = "HH:mm"
private const val TIME_PATTERN_12 = "hh:mm a"

private val russianDateFormatter by lazy {
    JavaDateTimeFormatter.ofPattern(DATE_PATTERN, Locale.forLanguageTag("ru"))
}

private val englishDateFormatter by lazy {
    JavaDateTimeFormatter.ofPattern(DATE_PATTERN, Locale.ENGLISH)
}

private val time24Formatter by lazy {
    JavaDateTimeFormatter.ofPattern(TIME_PATTERN_24)
}

private val russianTime12Formatter by lazy {
    JavaDateTimeFormatter.ofPattern(TIME_PATTERN_12, Locale.forLanguageTag("ru"))
}

private val englishTime12Formatter by lazy {
    JavaDateTimeFormatter.ofPattern(TIME_PATTERN_12, Locale.ENGLISH)
}

private fun actualDateFormatter(context: Context) =
    if (context.resources.configuration.locales[0].language == "ru") {
        russianDateFormatter
    } else {
        englishDateFormatter
    }

private fun actualTimeFormatter(context: Context) =
    if (DateFormat.is24HourFormat(context)) {
        time24Formatter
    } else if (context.resources.configuration.locales[0].language == "ru") {
        russianTime12Formatter
    } else {
        englishTime12Formatter
    }

class AndroidDateTimeFormatter(private val context: Context) : DateTimeFormatter {

    override fun format(date: LocalDate): String =
        actualDateFormatter(context).format(date.toJavaLocalDate())

    override fun format(time: LocalTime): String =
        actualTimeFormatter(context).format(time.toJavaLocalTime())
}
