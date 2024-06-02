package epicarchitect.breakbadhabits.data.resources.strings

interface DurationFormattingStrings {
    fun secondsText(): String
    fun minutesText(): String
    fun hoursText(): String
    fun daysText(): String
}

class RussianDurationFormattingStrings : DurationFormattingStrings {
    override fun secondsText() = "с"
    override fun minutesText() = "м"
    override fun hoursText() = "ч"
    override fun daysText() = "д"
}

class EnglishDurationFormattingStrings : DurationFormattingStrings {
    override fun secondsText() = "s"
    override fun minutesText() = "m"
    override fun hoursText() = "h"
    override fun daysText() = "d"
}