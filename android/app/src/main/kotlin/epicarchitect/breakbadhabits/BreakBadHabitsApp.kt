package epicarchitect.breakbadhabits

import android.app.Application

class BreakBadHabitsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        setupAppModuleHolder(this)
    }
}