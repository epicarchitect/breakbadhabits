package breakbadhabits.app.database

import android.content.Context

class IdGenerator(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(
        SHARED_PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )

    init {
        migrateToLong()
    }

    fun nextId(): Long {
        val lastId = sharedPreferences.getLong(LAST_ID_KEY, 0)
        val newId = lastId + 1
        setLastId(newId)
        return newId
    }

    fun setLastId(lastId: Long) {
        sharedPreferences.edit().putLong(LAST_ID_KEY, lastId).apply()
    }

    private fun migrateToLong() {
        val editor = sharedPreferences.edit()
        sharedPreferences.all.forEach {
            if (it.key == OLD_LAST_ID_KEY) {
                editor.putLong(LAST_ID_KEY, (it.value as Int).toLong())
            }
        }
        editor.apply()
    }

    companion object {
        private const val SHARED_PREFERENCES_NAME = "IdGenerator"
        private const val LAST_ID_KEY = "lastGeneratedId"
        private const val OLD_LAST_ID_KEY = "lastId"
    }
}