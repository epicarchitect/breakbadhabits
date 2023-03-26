package breakbadhabits.android.app

import android.app.Application
import breakbadhabits.android.app.appwidget.HabitsAppWidgetProvider
import breakbadhabits.android.app.di.LogicModule
import breakbadhabits.android.app.di.UiModule
import breakbadhabits.android.app.di.PresentationModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn

class BreakBadHabitsApp : Application() {

    val logicModule by lazy {
        LogicModule(
            context = this,
            databaseName = MAIN_DATABASE_NAME
        )
    }
    val presentationModule by lazy {
        PresentationModule(
            logicModule = logicModule
        )
    }
    val uiModule by lazy {
        UiModule(
            context = this, // TODO check resources working
            presentationModule = presentationModule
        )
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        registerAppWidgetUpdates()
    }

    private fun registerAppWidgetUpdates() {
        combine(
            logicModule.habitProvider.habitsFlow(),
            logicModule.habitTrackProvider.habitTracksFlow(),
            logicModule.habitAppWidgetConfigProvider.provideAllFlow()
        ) { _, _, _ ->
            HabitsAppWidgetProvider.sendUpdateIntent(this@BreakBadHabitsApp)
        }.launchIn(CoroutineScope(logicModule.coroutineDispatchers.default))
    }

    companion object {
        lateinit var instance: BreakBadHabitsApp
        private const val MAIN_DATABASE_NAME = "breakbadhabits-main"
    }
}