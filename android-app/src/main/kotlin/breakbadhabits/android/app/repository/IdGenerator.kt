package breakbadhabits.android.app.repository

import android.content.Context
import androidx.core.content.edit

class IdGenerator(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(
        SHARED_PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )

    fun nextId(): Int {
        val lastId = sharedPreferences.getInt(LAST_ID_KEY, 0)
        val newId = lastId + 1
        setLastId(newId)
        return newId
    }

    fun setLastId(lastId: Int) {
        sharedPreferences.edit { putInt(LAST_ID_KEY, lastId) }
    }

    companion object {
        private const val SHARED_PREFERENCES_NAME = "IdGenerator"
        private const val LAST_ID_KEY = "lastId"
    }
}