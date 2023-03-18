package breakbadhabits.android.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import breakbadhabits.android.app.format.DateTimeFormatter
import breakbadhabits.android.app.format.DurationFormatter
import breakbadhabits.android.app.ui.app.AppRootScreen
import breakbadhabits.android.app.ui.app.LocalDateTimeFormatter
import breakbadhabits.android.app.ui.app.LocalDurationFormatter
import breakbadhabits.android.app.ui.app.LocalHabitIconResourceProvider
import breakbadhabits.android.app.ui.app.LocalPresentationModule
import breakbadhabits.android.app.ui.habits.resources.HabitIconResourceProvider
import breakbadhabits.foundation.uikit.activity.ComposeActivity

class MainActivity : ComposeActivity() {

    override val themeResourceId = R.style.Activity

    @Composable
    override fun Content() {
        val presentationModule = BreakBadHabitsApp.instance.presentationModule
        CompositionLocalProvider(
            LocalPresentationModule provides presentationModule,
            LocalHabitIconResourceProvider provides HabitIconResourceProvider(
                habitIconProvider = presentationModule.logicModule.habitIconProvider
            ),
            LocalDateTimeFormatter provides DateTimeFormatter(),
            LocalDurationFormatter provides DurationFormatter(
                resources = resources,
                defaultAccuracy = DurationFormatter.Accuracy.SECONDS
            )
        ) {
            AppRootScreen()
        }
    }
}