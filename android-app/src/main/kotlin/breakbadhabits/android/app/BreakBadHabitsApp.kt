package breakbadhabits.android.app

import android.app.Application
import breakbadhabits.android.app.di.logic.LogicModule
import breakbadhabits.android.app.di.presentation.PresentationModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class BreakBadHabitsApp : Application() {

    val presentationModule by lazy {
        PresentationModule(
            LogicModule(context = this)
        )
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        CoroutineScope(Dispatchers.IO).launch {

        }
    }

    companion object {
        lateinit var instance: BreakBadHabitsApp
    }
}