package epicarchitect.breakbadhabits.operation.datetime

import kotlin.time.Duration

fun Duration.secondsPart() = inWholeSeconds % 60

fun Duration.minutesPart() = inWholeSeconds / 60 % 60

fun Duration.hoursPart() = inWholeSeconds / 60 / 60 % 24

fun Duration.daysPart() = inWholeSeconds / 60 / 60 / 24

fun Duration?.orZero() = this ?: Duration.ZERO