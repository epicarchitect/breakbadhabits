package epicarchitect.breakbadhabits

import android.app.Application
import epicarchitect.breakbadhabits.database.SqlDriverFactory

class BreakBadHabitsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SqlDriverFactory.context = this
    }
}