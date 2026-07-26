package epicarchitect.breakbadhabits.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

class IosDataStoreFactory : PlatformDataStoreFactory {
    @OptIn(ExperimentalForeignApi::class)
    override fun create(name: String): DataStore<Preferences> {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )!!

        return PreferenceDataStoreFactory.createWithPath(
            produceFile = { "${documentDirectory.path}/$name".toPath() }
        )
    }
}