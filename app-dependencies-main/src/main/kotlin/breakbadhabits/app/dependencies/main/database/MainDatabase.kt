package breakbadhabits.app.dependencies.main.database

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
internal abstract class MainDatabase : RoomDatabase() {
    abstract val habitsDao: HabitsDao
    abstract val habitTracksDao: HabitTracksDao
}