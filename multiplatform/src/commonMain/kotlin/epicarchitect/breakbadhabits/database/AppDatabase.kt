package epicarchitect.breakbadhabits.database

import androidx.room3.ColumnTypeConverters
import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor

@ConstructedBy(AppDatabaseConstructor::class)
@Database(
    entities = [
        Habit::class,
        HabitEventRecord::class,
        PremiumStatus::class,
        PremiumOfferInfo::class,
        HabitWidget::class,
        AppSettings::class,
    ],
    version = 2,
    exportSchema = true
)
@ColumnTypeConverters(
    InstantConverter::class,
    UuidConverter::class,
    HabitIdsConverter::class,
    HabitOrderConverter::class,
)
abstract class AppDatabase : RoomDatabase() {
    abstract val habitsDao: HabitsDao

    abstract val habitEventRecordsDao: HabitEventRecordsDao

    abstract val premiumStatusDao: PremiumStatusDao

    abstract val premiumOfferInfoDao: PremiumOfferInfoDao

    abstract val habitWidgetDao: HabitWidgetDao

    abstract val appSettingsDao: AppSettingsDao

}

expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
