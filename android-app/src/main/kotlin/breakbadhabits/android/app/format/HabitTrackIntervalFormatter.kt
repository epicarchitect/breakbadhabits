package breakbadhabits.android.app.format

import breakbadhabits.entity.HabitTrack

class HabitTrackIntervalFormatter {
    fun format(track: HabitTrack.Range) = "${track.value.start} - ${track.value.endInclusive}"
}