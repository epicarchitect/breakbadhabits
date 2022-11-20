package breakbadhabits.android.app

import android.app.Application
import breakbadhabits.app.dependencies.main.MainAppDependencies

class BreakBadHabitsApp : Application() {

    val dependencies by lazy {
        MainAppDependencies(this)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: BreakBadHabitsApp
    }
}