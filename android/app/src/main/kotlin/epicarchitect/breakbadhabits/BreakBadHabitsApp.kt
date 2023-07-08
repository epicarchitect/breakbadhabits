package epicarchitect.breakbadhabits

import android.app.Application
import epicarchitect.breakbadhabits.di.holder.AppModuleHolder
import epicarchitect.breakbadhabits.habits.widget.android.HabitsAppWidgetProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn

class BreakBadHabitsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        setupAppModuleHolder(this)
        registerAppWidgetUpdates(this)
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