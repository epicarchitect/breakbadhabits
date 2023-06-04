package epicarchitect.breakbadhabits.android.app

import androidx.compose.runtime.Composable
import epicarchitect.breakbadhabits.android.app.base.activity.ComposeActivity
import epicarchitect.breakbadhabits.ui.app.AppScreen
import epicarchitect.breakbadhabits.di.holder.AppModuleHolder

class MainActivity : ComposeActivity() {

    override val themeResourceId = R.style.Activity

    @Composable
    override fun Content() {
        AppScreen(AppModuleHolder.current)
    }
}