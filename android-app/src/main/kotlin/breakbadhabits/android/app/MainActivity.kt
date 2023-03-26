package breakbadhabits.android.app

import androidx.compose.runtime.Composable
import breakbadhabits.android.app.base.activity.ComposeActivity
import breakbadhabits.android.app.ui.app.AppScreen

class MainActivity : ComposeActivity() {

    override val themeResourceId = R.style.Activity

    @Composable
    override fun Content() {
        AppScreen(BreakBadHabitsApp.instance.uiModule)
    }
}