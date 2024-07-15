package epicarchitect.breakbadhabits.environment.language

import epicarchitect.breakbadhabits.environment.database.AppDatabase
import epicarchitect.breakbadhabits.environment.database.AppSettings
import epicarchitect.breakbadhabits.environment.database.AppSettingsLanguage
import epicarchitect.breakbadhabits.operation.sqldelight.flowOfOneOrNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn

class ActualAppLanguage(
    coroutineScope: CoroutineScope,
    database: AppDatabase
) {
    val state = combine(
        ActualSystemLanguage.state,
        database.appSettingsQueries.settings().flowOfOneOrNull().filterNotNull(),
        ::resolve
    ).stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = resolve(
            systemLanguage = ActualSystemLanguage.state.value,
            settings = database.appSettingsQueries.settings().executeAsOne()
        )
    )

    private fun resolve(
        systemLanguage: AppLanguage,
        settings: AppSettings
    ) = when (settings.language) {
        AppSettingsLanguage.SYSTEM -> systemLanguage
        AppSettingsLanguage.RUSSIAN -> AppLanguage.RUSSIAN
        AppSettingsLanguage.ENGLISH -> AppLanguage.ENGLISH
    }
}