package breakbadhabits.android.app

import android.app.Application
import breakbadhabits.android.app.di.logic.LogicModule
import breakbadhabits.android.app.di.presentation.PresentationModule

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