package breakbadhabits.android.app.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        Habit::class,
        HabitTrack::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MainDatabase : RoomDatabase() {
    abstract val habitsDao: HabitsDao
    abstract val habitTracksDao: HabitTracksDao
}