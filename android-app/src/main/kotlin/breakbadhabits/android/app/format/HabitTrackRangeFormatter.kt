package breakbadhabits.android.app.format

import breakbadhabits.entity.HabitTrack
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter

class HabitTrackRangeFormatter {
    private val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
    fun format(track: HabitTrack.Range): String {
        val start = track.value.start.date.toJavaLocalDate()
        val end = track.value.endInclusive.date.toJavaLocalDate()
        return "${formatter.format(start)} - ${formatter.format(end)}"
    }
}