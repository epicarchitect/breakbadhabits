package epicarchitect.breakbadhabits.screens.habits.details

import epicarchitect.breakbadhabits.screens.root.RootRoute
import kotlin.uuid.Uuid

fun parseHabitDetailsDeepLink(rawHabitId: String?): RootRoute.HabitDetails? =
    rawHabitId
        ?.let { raw -> runCatching { Uuid.parse(raw) }.getOrNull() }
        ?.let(RootRoute::HabitDetails)
