package epicarchitect.breakbadhabits

import androidx.compose.ui.window.ComposeUIViewController
import epicarchitect.breakbadhabits.database.IosSqlDriverFactory
import epicarchitect.breakbadhabits.datetime.format.IosDateTimeFormatter
import epicarchitect.breakbadhabits.environment.AppEnvironment
import epicarchitect.breakbadhabits.language.ComposeBasedLanguageProvider
import epicarchitect.breakbadhabits.screens.root.RootScreen

val defaultAppEnvironment by lazy {
    AppEnvironment(
        platformSqlDriverFactory = IosSqlDriverFactory(),
        platformLanguageProvider = ComposeBasedLanguageProvider(),
        platformDateTimeFormatter = IosDateTimeFormatter()
    )
}


@Suppress("unused")
fun createAppViewController() = ComposeUIViewController(
    configure = {
        enforceStrictPlistSanityCheck = false
    },
    content = {
        RootScreen(defaultAppEnvironment)
    }
)