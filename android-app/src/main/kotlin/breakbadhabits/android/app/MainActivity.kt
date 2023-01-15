package breakbadhabits.android.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import breakbadhabits.android.app.format.HabitAbstinenceFormatter
import breakbadhabits.android.app.format.HabitTrackRangeFormatter
import breakbadhabits.android.app.resources.HabitIconResources
import breakbadhabits.ui.kit.activity.ComposeActivity

class MainActivity : ComposeActivity() {

    override val themeResourceId = R.style.Activity

    @Composable
    override fun Content() {
        CompositionLocalProvider(
            LocalPresentationModule provides BreakBadHabitsApp.instance.presentationModule,
            LocalHabitIconResources provides HabitIconResources(LocalContext.current),
            LocalHabitAbstinenceFormatter provides HabitAbstinenceFormatter(LocalContext.current),
            LocalHabitTrackRangeFormatter provides HabitTrackRangeFormatter()
        ) {
            AppRootScreen()
        }
    }
}