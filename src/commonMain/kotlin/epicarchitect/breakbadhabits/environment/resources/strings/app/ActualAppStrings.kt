package epicarchitect.breakbadhabits.environment.resources.strings.app

import epicarchitect.breakbadhabits.environment.database.AppDatabase
import epicarchitect.breakbadhabits.environment.database.AppSettings
import epicarchitect.breakbadhabits.environment.database.AppSettingsLanguage
import epicarchitect.breakbadhabits.environment.language.ActualSystemLanguage
import epicarchitect.breakbadhabits.environment.language.AppLanguage
import epicarchitect.breakbadhabits.operation.sqldelight.flowOfOneOrNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn

class ActualAppStrings(
    coroutineScope: CoroutineScope,
    database: AppDatabase
) {
    val state = combine(
        ActualSystemLanguage.state,
        database.appSettingsQueries.settings().flowOfOneOrNull().filterNotNull(), 
        ::resolveAppStrings
    ).stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = resolveAppStrings(
            systemLanguage = ActualSystemLanguage.state.value,
            settings = database.appSettingsQueries.settings().executeAsOne()
        )
    )
    
    private fun resolveAppStrings(
        systemLanguage: AppLanguage,
        settings: AppSettings
    ) = when (settings.language) {
        AppSettingsLanguage.SYSTEM -> LocalizedAppStrings(systemLanguage)
        AppSettingsLanguage.RUSSIAN -> LocalizedAppStrings(AppLanguage.RUSSIAN)
        AppSettingsLanguage.ENGLISH -> LocalizedAppStrings(AppLanguage.ENGLISH)
    }
}