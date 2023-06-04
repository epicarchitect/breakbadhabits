package epicarchitect.breakbadhabits.database

import android.content.Context

class AndroidIdGenerator(context: Context) : IdGenerator {

    private val sharedPreferences = context.getSharedPreferences(
        SHARED_PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )

    override fun nextId(): Int {
        val lastId = sharedPreferences.getInt(LAST_ID_KEY, 0)
        val newId = lastId + 1
        setLastId(newId)
        return newId
    }

    override fun setLastId(lastId: Int) {
        sharedPreferences.edit().putInt(LAST_ID_KEY, lastId).apply()
    }

    companion object {
        private const val SHARED_PREFERENCES_NAME = "IdGenerator"
        private const val LAST_ID_KEY = "lastId"
    }
}