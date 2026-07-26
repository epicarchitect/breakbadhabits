package epicarchitect.breakbadhabits.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

interface PlatformDataStoreFactory {
    fun create(name: String): DataStore<Preferences>
}