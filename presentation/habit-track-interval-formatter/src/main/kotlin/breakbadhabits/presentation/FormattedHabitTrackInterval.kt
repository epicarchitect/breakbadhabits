package breakbadhabits.presentation

import breakbadhabits.entity.HabitTrack

data class FormattedHabitTrackInterval(
    val track: HabitTrack.Interval,
    val result: String
) {
    override fun toString() = result
}