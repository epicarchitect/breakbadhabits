package breakbadhabits.android.app.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        Habit::class,
        HabitTrack::class,
        HabitsAppWidgetConfig::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MainDatabase : RoomDatabase() {
    abstract val habitDao: HabitDao
    abstract val habitTracksDao: HabitTracksDao
    abstract val habitsAppWidgetConfigDao: HabitsAppWidgetConfigDao
}