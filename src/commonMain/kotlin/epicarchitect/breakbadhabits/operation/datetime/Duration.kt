package epicarchitect.breakbadhabits.operation.datetime

import kotlin.time.Duration

val Duration.onlySeconds get() = inWholeSeconds % 60

val Duration.onlyMinutes get() = inWholeSeconds / 60 % 60

val Duration.onlyHours get() = inWholeSeconds / 60 / 60 % 24

val Duration.onlyDays get() = inWholeSeconds / 60 / 60 / 24