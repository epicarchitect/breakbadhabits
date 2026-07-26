package epicarchitect.breakbadhabits.datastore

import android.app.Application
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import okio.Path.Companion.toPath

class AndroidDataStoreFactory(
    private val application: Application
) : PlatformDataStoreFactory {
    override fun create(name: String) = PreferenceDataStoreFactory.createWithPath(
        produceFile = {
            application.filesDir.resolve(name).absolutePath.toPath()
        }
    )
}