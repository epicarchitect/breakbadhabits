package epicarchitect.breakbadhabits.defaultDependencies.habits.tracks.list

import androidx.compose.ui.text.intl.Locale
import epicarchitect.breakbadhabits.features.habits.tracks.list.HabitTracksResources


class RussianHabitTracksResources : HabitTracksResources {
    override fun title(): String = "habitTracks"
    override fun habitTrackNoComment() = "no commnet"
    override fun newEventButton() = "new"
}

class EnglishHabitTracksResources : HabitTracksResources {
    override fun title(): String = "habitTracks"
    override fun habitTrackNoComment() = "no commnet"
    override fun newEventButton() = "new"
}

class LocalizedHabitTracksResources(locale: Locale) : HabitTracksResources by (
    if (locale.language == "ru") {
        RussianHabitTracksResources()
    } else {
        EnglishHabitTracksResources()
    }
    )