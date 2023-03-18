package breakbadhabits.android.app

import android.app.Application
import breakbadhabits.app.di.LogicModule
import breakbadhabits.app.di.PresentationModule

class BreakBadHabitsApp : Application() {

    val presentationModule by lazy {
        PresentationModule(
            LogicModule(
                context = this,
                databaseName = MAIN_DATABASE_NAME
            )
        )
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: BreakBadHabitsApp
        private const val MAIN_DATABASE_NAME = "breakbadhabits-main"
    }
}