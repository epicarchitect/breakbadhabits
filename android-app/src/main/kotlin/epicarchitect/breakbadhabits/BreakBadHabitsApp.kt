package epicarchitect.breakbadhabits

import android.app.Application
import epicarchitect.breakbadhabits.database.AndroidSqlDriverFactory
import epicarchitect.breakbadhabits.datetime.format.AndroidDateTimeFormatter
import epicarchitect.breakbadhabits.environment.AppEnvironment
import epicarchitect.breakbadhabits.language.ComposeBasedLanguageProvider

lateinit var appEnvironment: AppEnvironment

class BreakBadHabitsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val languageProvider = ComposeBasedLanguageProvider()
        appEnvironment = AppEnvironment(
            platformSqlDriverFactory = AndroidSqlDriverFactory(
                context = this
            ),
            platformLanguageProvider = languageProvider,
            platformDateTimeFormatter = AndroidDateTimeFormatter(
                context = this,
                languageProvider = languageProvider
            )
        )
    }
}