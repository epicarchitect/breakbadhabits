package epicarchitect.breakbadhabits

import android.app.Application
import epicarchitect.breakbadhabits.di.holder.AppModuleHolder
import epicarchitect.breakbadhabits.habits.widget.android.HabitsAppWidgetProvider
import epicarchitect.breakbadhabits.presentation.app.AppViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn

lateinit var appViewModel: AppViewModel

class BreakBadHabitsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        setupAppModuleHolder(this)
        registerAppWidgetUpdates(this)
        appViewModel = AppViewModel(CoroutineScope(Dispatchers.Main), AppModuleHolder.require())
    }
}

private fun registerAppWidgetUpdates(
    app: Application
) = with(AppModuleHolder.logic.habits) {
    combine(
        habitProvider.habitsFlow(),
        habitTrackProvider.habitTracksFlow(),
        habitWidgetProvider.provideAllFlow()
    ) { _, _, _ ->
        HabitsAppWidgetProvider.sendUpdateIntent(app)
    }.launchIn(CoroutineScope(Dispatchers.Default))
}