package epicarchitect.breakbadhabits.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import epicarchitect.breakbadhabits.database.AppSettings
import epicarchitect.breakbadhabits.infrastructure.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

private val DefaultAppSettings = AppSettings(
    id = 0,
    habitOrder = emptyList()
)

private val state = AppDatabase.appSettingsDao.appSettings().stateIn(
    scope = CoroutineScope(Dispatchers.Default),
    started = SharingStarted.Eagerly,
    initialValue = null
)

@Composable
fun rememberAppSettings(): AppSettings {
    val appSettings by state.collectAsState()
    return appSettings ?: DefaultAppSettings
}
