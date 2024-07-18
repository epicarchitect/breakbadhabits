package epicarchitect.breakbadhabits

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.core.content.edit
import androidx.core.database.getStringOrNull
import epicarchitect.breakbadhabits.environment.Environment
import epicarchitect.breakbadhabits.environment.database.ListOfIntAdapter
import kotlinx.datetime.Instant

class MigrationToV4(private val context: Context) {

    private val preferences = context.getSharedPreferences("MigrationToV4", Context.MODE_PRIVATE)
    private var isExecuted
        set(value) = preferences.edit { putBoolean("isExecuted", value) }
        get() = preferences.getBoolean("isExecuted", false)

    @SuppressLint("Range")
    fun executeIfNeeded() {
        if (isExecuted) return
        isExecuted = true

        val database = context.openOrCreateDatabase("main.db", Context.MODE_PRIVATE, null)

        try {
            database.migrateHabits()
            database.migrateHabitEventRecords()
            database.migrateHabitWidgets()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            database.close()
        }
    }

    @SuppressLint("Range")
    private fun SQLiteDatabase.migrateHabits() {
        query("habits", null, null, null, null, null, null).use {
            while (it.moveToNext()) {
                Environment.database.habitQueries.insert(
                    id = it.getInt(it.getColumnIndex("id")),
                    name = it.getString(it.getColumnIndex("name")),
                    iconId = it.getInt(it.getColumnIndex("iconId"))
                )
            }
        }
    }

    @SuppressLint("Range")
    private fun SQLiteDatabase.migrateHabitEventRecords() {
        query("habitEvents", null, null, null, null, null, null).use {
            while (it.moveToNext()) {
                Environment.database.habitEventRecordQueries.insert(
                    habitId = it.getInt(it.getColumnIndex("habitId")),
                    startTime = it.getLong(it.getColumnIndex("time")).let {
                        Instant.fromEpochMilliseconds(it)
                    },
                    endTime = it.getLong(it.getColumnIndex("time")).let {
                        Instant.fromEpochMilliseconds(it)
                    },
                    comment = it.getStringOrNull(it.getColumnIndex("comment")).orEmpty(),
                    eventCount = 1
                )
            }
        }
    }

    @SuppressLint("Range")
    private fun SQLiteDatabase.migrateHabitWidgets() {
        query("habitsAppWidgetConfigs", null, null, null, null, null, null).use {
            while (it.moveToNext()) {
                Environment.database.habitWidgetQueries.insert(
                    title = it.getString(it.getColumnIndex("title")),
                    systemId = it.getInt(it.getColumnIndex("appWidgetId")),
                    habitIds = it.getString(it.getColumnIndex("habitIdsJson")).let {
                        ListOfIntAdapter.decode(it)
                    }
                )
            }
        }
    }
}

