package breakbadhabits.android.app.time

import java.util.Calendar

fun Calendar.copy(setup: Calendar.() -> Unit = {}) = (clone() as Calendar).apply(setup)

fun monthEquals(calendar1: Calendar, calendar2: Calendar) =
    calendar1[Calendar.MONTH] == calendar2[Calendar.MONTH] && calendar1[Calendar.YEAR] == calendar2[Calendar.YEAR]

fun Calendar.setStartDayValues() {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}

fun Calendar.setEndDayValues() {
    set(Calendar.HOUR_OF_DAY, 23)
    set(Calendar.MINUTE, 59)
    set(Calendar.SECOND, 59)
    set(Calendar.MILLISECOND, 999)
}

fun Calendar.setStartMonthValues() {
    set(Calendar.DAY_OF_MONTH, 1)
    setStartDayValues()
}

fun Calendar.setEndMonthValues() {
    set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
    setEndDayValues()
}
