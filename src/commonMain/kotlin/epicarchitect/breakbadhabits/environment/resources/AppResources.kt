package epicarchitect.breakbadhabits.environment.resources

import epicarchitect.breakbadhabits.environment.database.AppDatabase
import epicarchitect.breakbadhabits.environment.resources.icons.AppIcons
import epicarchitect.breakbadhabits.environment.resources.strings.app.ActualAppStrings
import kotlinx.coroutines.CoroutineScope

class AppResources(
    coroutineScope: CoroutineScope,
    database: AppDatabase
) {
    val strings = ActualAppStrings(coroutineScope, database)
    val icons = AppIcons()
}