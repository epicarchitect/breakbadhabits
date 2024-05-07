package epicarchitect.breakbadhabits

import android.app.Application
import epicarchitect.breakbadhabits.data.SqlDriverFactory

class BreakBadHabitsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SqlDriverFactory.context = this
    }
}