package breakbadhabits.presentation

import breakbadhabits.entity.HabitTrack

class HabitTrackIntervalFormatter {

    fun format(track: HabitTrack.Interval) = FormattedHabitTrackInterval(
        track = track,
        result = track.value.start.toString() + " - " + track.value.end.toString()
    )
}