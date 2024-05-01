package epicarchitect.breakbadhabits.screens.habits.tracks.list

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.intl.Locale

val LocalHabitTracksResources = compositionLocalOf {
    if (Locale.current.language == "ru") {
        RussianHabitTracksResources()
    } else {
        EnglishHabitTracksResources()
    }
}

interface HabitTracksResources {
    fun title(): String
    fun habitTrackNoComment(): String
    fun newEventButton(): String
}

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