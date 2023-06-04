package epicarchitect.breakbadhabits.android.app

import androidx.compose.runtime.Composable
import epicarchitect.breakbadhabits.android.app.base.activity.ComposeActivity
import epicarchitect.breakbadhabits.android.app.ui.app.AppScreen

class MainActivity : ComposeActivity() {

    override val themeResourceId = R.style.Activity

    @Composable
    override fun Content() {
        AppScreen(BreakBadHabitsApp.instance.uiModule)
    }
}