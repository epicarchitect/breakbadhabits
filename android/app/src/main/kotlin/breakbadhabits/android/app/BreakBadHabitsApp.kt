package breakbadhabits.android.app

import android.app.Application
import breakbadhabits.android.app.appwidget.HabitsAppWidgetProvider
import breakbadhabits.android.app.di.AppDependencies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn

class BreakBadHabitsApp : Application() {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        instance = this
        combine(
            appDependencies.habitsRepository.habitListFlow(),
            appDependencies.habitsRepository.habitEventListFlow(),
            appDependencies.appWidgetsRepository.habitsAppWidgetConfigListFlow()
        ) { _, _, _ ->
            HabitsAppWidgetProvider.sendUpdateIntent(this@BreakBadHabitsApp)
        }.launchIn(coroutineScope)
    }

    companion object {
        lateinit var instance: BreakBadHabitsApp
    }
}

val appDependencies by lazy {
    AppDependencies(BreakBadHabitsApp.instance)
}