package breakbadhabits.android.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import breakbadhabits.app.ui.App
import breakbadhabits.app.ui.LocalAppDependencies
import breakbadhabits.ui.kit.activity.ComposeActivity

class MainActivity : ComposeActivity() {

    override val themeResourceId = R.style.Activity

    @Composable
    override fun Content() {
        CompositionLocalProvider(
            LocalAppDependencies provides BreakBadHabitsApp.instance.dependencies
        ) {
            App()
        }
    }
}
