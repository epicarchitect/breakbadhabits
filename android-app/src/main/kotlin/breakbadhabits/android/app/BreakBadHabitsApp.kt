package breakbadhabits.android.app

import android.app.Application
import breakbadhabits.app.dependecies.LogicModule
import breakbadhabits.app.dependecies.PresentationModule

class BreakBadHabitsApp : Application() {

    val presentationModule by lazy {
        PresentationModule(
            LogicModule(context = this)
        )
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: BreakBadHabitsApp
    }
}