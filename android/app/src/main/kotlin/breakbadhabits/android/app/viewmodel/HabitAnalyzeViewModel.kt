package breakbadhabits.android.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import breakbadhabits.android.app.data.HabitEventData
import breakbadhabits.android.app.repository.HabitsRepository
import breakbadhabits.android.app.time.copy
import breakbadhabits.android.app.time.setEndDayValues
import breakbadhabits.android.app.time.setStartDayValues
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

class HabitAnalyzeViewModel(
    habitsRepository: HabitsRepository,
    habitId: Int,
) : ViewModel() {

    val allEventsFlow = habitsRepository.habitEventListByHabitIdFlow(habitId).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )
    val filteredEventsStateProcessFlow = MutableStateFlow<List<HabitEventData>>(emptyList())
    val timeIntervalsStateFlow = MutableStateFlow<List<TimeInterval>>(emptyList())
    val selectedTimeIntervalStateFlow = MutableStateFlow<TimeInterval?>(null)

    init {
        allEventsFlow.onEach { habitEvents ->
            val currentCalendar = Calendar.getInstance()
            currentCalendar.setEndDayValues()

            timeIntervalsStateFlow.update {
                listOf(
                    TimeInterval.Last7Days(
                        currentCalendar.copy {
                            timeInMillis -= 7L * 24L * 60L * 60L * 1000L
                            setStartDayValues()
                        }.timeInMillis,
                        currentCalendar.timeInMillis
                    ),
                    TimeInterval.Last30Days(
                        currentCalendar.copy {
                            timeInMillis -= 30L * 24L * 60L * 60L * 1000L
                            setStartDayValues()
                        }.timeInMillis,
                        currentCalendar.timeInMillis
                    ),
                    TimeInterval.Last60Days(
                        currentCalendar.copy {
                            timeInMillis -= 60L * 24L * 60L * 60L * 1000L
                            setStartDayValues()
                        }.timeInMillis,
                        currentCalendar.timeInMillis
                    ),
                    TimeInterval.Last90Days(
                        currentCalendar.copy {
                            timeInMillis -= 90L * 24L * 60L * 60L * 1000L
                            setStartDayValues()
                        }.timeInMillis,
                        currentCalendar.timeInMillis
                    ),
                    TimeInterval.AllDays(
                        currentCalendar.copy {
                            timeInMillis = habitEvents.minByOrNull { it.time }?.time ?: -1
                            setStartDayValues()
                        }.timeInMillis,
                        currentCalendar.timeInMillis
                    )
                )
            }
        }.launchIn(viewModelScope)

        timeIntervalsStateFlow.onEach {
            if (it.isNotEmpty() && selectedTimeIntervalStateFlow.value == null) {
                setTimeInterval(it.first())
            }
        }.launchIn(viewModelScope)

        allEventsFlow.combine(selectedTimeIntervalStateFlow) { allEvents, timeInterval ->
            if (timeInterval != null) {
                filteredEventsStateProcessFlow.update {
                    allEvents.filter {
                        it.time in timeInterval.startTime..timeInterval.endTime
                    }.sortedBy { it.time }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun setTimeInterval(timeInterval: TimeInterval) = viewModelScope.launch {
        selectedTimeIntervalStateFlow.update {
            timeInterval
        }
    }

    sealed class TimeInterval {
        abstract val startTime: Long
        abstract val endTime: Long

        class Last7Days(override val startTime: Long, override val endTime: Long) : TimeInterval()
        class Last30Days(override val startTime: Long, override val endTime: Long) : TimeInterval()
        class Last60Days(override val startTime: Long, override val endTime: Long) : TimeInterval()
        class Last90Days(override val startTime: Long, override val endTime: Long) : TimeInterval()
        class AllDays(override val startTime: Long, override val endTime: Long) : TimeInterval()
    }
}