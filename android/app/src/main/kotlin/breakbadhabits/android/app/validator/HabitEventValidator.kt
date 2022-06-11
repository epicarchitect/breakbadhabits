package breakbadhabits.android.app.validator

class HabitEventValidator(private val getCurrentTimeInMillis: () -> Long) {

    fun timeNotBiggestThenCurrentTime(time: Long) = time <= getCurrentTimeInMillis()

}