package epicarchitect.breakbadhabits.features

import androidx.compose.ui.text.intl.Locale
import epicarchitect.breakbadhabits.features.habits.tracks.HabitTracksResources

fun habitTracksResourcesOf(locale: Locale) = when (locale.language) {
    "ru" -> object : HabitTracksResources {
        override val newEventButton = "Добавить новые события"
        override val habitTrackNoComment = "Комментарий отсутствует."
    }

    else -> object : HabitTracksResources {
        override val newEventButton = "Add new events"
        override val habitTrackNoComment = "There is no comment."
    }
}

