package epicarchitect.breakbadhabits.data.resources.strings

interface HabitTracksStrings {
    fun title(): String
    fun habitTrackNoComment(): String
    fun newEventButton(): String
}

class RussianHabitTracksStrings : HabitTracksStrings {
    override fun title(): String = "habitTracks"
    override fun habitTrackNoComment() = "no commnet"
    override fun newEventButton() = "new"
}

class EnglishHabitTracksStrings : HabitTracksStrings {
    override fun title(): String = "habitTracks"
    override fun habitTrackNoComment() = "no commnet"
    override fun newEventButton() = "new"
}