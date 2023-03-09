package breakbadhabits.android.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import breakbadhabits.android.app.ui.LocalHabitIconResources
import breakbadhabits.android.app.ui.LocalPresentationModule
import breakbadhabits.android.app.ui.habits.resources.HabitIconResources
import breakbadhabits.android.app.ui.root.AppRootScreen
import breakbadhabits.foundation.uikit.activity.ComposeActivity

class MainActivity : ComposeActivity() {

    override val themeResourceId = R.style.Activity

    @Composable
    override fun Content() {
        CompositionLocalProvider(
            LocalPresentationModule provides BreakBadHabitsApp.instance.presentationModule,
            LocalHabitIconResources provides HabitIconResources(LocalContext.current)
        ) {
            AppRootScreen()
        }
    }
}