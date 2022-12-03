package breakbadhabits.android.app.format

import breakbadhabits.entity.HabitTrack

class HabitTrackIntervalFormatter {
    fun format(track: HabitTrack.Interval) = "${track.value.start} - ${track.value.end}"
}