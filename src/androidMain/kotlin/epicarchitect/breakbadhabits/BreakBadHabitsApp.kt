package epicarchitect.breakbadhabits

import android.app.Application
import android.os.Looper
import epicarchitect.breakbadhabits.database.AndroidSqlDriverFactory
import epicarchitect.breakbadhabits.datastore.AndroidDataStoreFactory
import epicarchitect.breakbadhabits.datetime.format.AndroidDateTimeFormatter
import epicarchitect.breakbadhabits.environment.AppEnvironment
import epicarchitect.breakbadhabits.language.ComposeBasedLanguageProvider

lateinit var appEnvironment: AppEnvironment

class BreakBadHabitsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val languageProvider = ComposeBasedLanguageProvider()
        Looper.prepare()
        appEnvironment = AppEnvironment(
            platformSqlDriverFactory = AndroidSqlDriverFactory(
                application = this
            ),
            platformDataStoreFactory = AndroidDataStoreFactory(
                application = this
            ),
            platformLanguageProvider = languageProvider,
            platformDateTimeFormatter = AndroidDateTimeFormatter(
                application = this,
                languageProvider = languageProvider
            )
        )
    }
}