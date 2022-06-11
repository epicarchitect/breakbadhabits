package breakbadhabits.android.app.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        HabitEntity::class,
        HabitEventEntity::class,
        HabitsAppWidgetConfigEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MainDatabase : RoomDatabase() {
    abstract val habitDao: HabitDao
    abstract val habitEventDao: HabitEventDao
    abstract val habitsAppWidgetConfigDao: HabitsAppWidgetConfigDao
}