package epicarchitect.breakbadhabits.logic.habits.newarch.habits

import epicarchitect.breakbadhabits.foundation.icons.Icon
import epicarchitect.breakbadhabits.foundation.identification.IdGenerator
import epicarchitect.breakbadhabits.logic.habits.newarch.time.AppTime
import epicarchitect.breakbadhabits.logic.habits.validator.CorrectHabitNewName
import epicarchitect.breakbadhabits.logic.habits.validator.CorrectHabitTrackEventCount
import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

class DefaultHabitCreationOperation(
    private val idGenerator: IdGenerator,
    private val mainDatabase: MainDatabase,
    private val name: CorrectHabitNewName,
    private val icon: Icon,
    private val trackEventCount: CorrectHabitTrackEventCount,
    private val time: HabitTime,
    private val appTime: AppTime
) : HabitCreationOperation {
    override suspend fun execute() = withContext(Dispatchers.IO) {
        mainDatabase.transaction {
            val habitId = idGenerator.nextId()
            val trackId = idGenerator.nextId()

            val endTime = appTime.instant()
            val startTime = endTime - time.offset

            mainDatabase.habitQueries.insert(
                id = habitId,
                name = name.data,
                iconId = icon.id
            )

            mainDatabase.habitTrackQueries.insert(
                id = trackId,
                habitId = habitId,
                startTime = startTime,
                endTime = endTime,
                eventCount = trackEventCount.data,
                comment = ""
            )
        }
    }

    enum class HabitTime(val offset: Duration) {
        MONTH_1(30.days),
        MONTH_3(90.days),
        MONTH_6(180.days),
        YEAR_1(365.days),
        YEAR_2(365.days * 2),
        YEAR_3(365.days * 3),
        YEAR_4(365.days * 4),
        YEAR_5(365.days * 5),
        YEAR_6(365.days * 6),
        YEAR_7(365.days * 7),
        YEAR_8(365.days * 8),
        YEAR_9(365.days * 9),
        YEAR_10(365.days * 10)
    }
}