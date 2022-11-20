package breakbadhabits.android.app

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import breakbadhabits.android.app.appwidget.HabitsAppWidgetProvider
import breakbadhabits.android.app.di.AppDependenciesImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn

class BreakBadHabitsApp : Application() {

    val appDependencies by lazy {
        AppDependenciesImpl(this)
    }

//    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        instance = this
//        combine(
//            appDependencies.habitsRepository.habitsFlow(),
//            appDependencies.habitsRepository.habitTracksListFlow(),
//            appDependencies.appWidgetsRepository.habitsAppWidgetConfigListFlow()
//        ) { _, _, _ ->
//            HabitsAppWidgetProvider.sendUpdateIntent(this@BreakBadHabitsApp)
//        }.launchIn(coroutineScope)
//        ViewModel().viewModelScope
    }

    companion object {
        lateinit var instance: BreakBadHabitsApp
    }
}