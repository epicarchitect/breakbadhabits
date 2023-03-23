package breakbadhabits.android.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import breakbadhabits.android.app.format.DateTimeFormatter
import breakbadhabits.android.app.format.DurationFormatter
import breakbadhabits.android.app.ui.app.AppScreen
import breakbadhabits.android.app.ui.habits.resources.HabitIconResourceProvider
import breakbadhabits.android.app.ui.theme.AppColorsSchemes
import breakbadhabits.android.app.base.activity.ComposeActivity
import breakbadhabits.android.app.base.activity.LocalDarkModeManager
import breakbadhabits.foundation.uikit.theme.AppTheme

class MainActivity : ComposeActivity() {

    override val themeResourceId = R.style.Activity

    @Composable
    override fun Content() {
        val presentationModule = BreakBadHabitsApp.instance.presentationModule
        val darkMode by LocalDarkModeManager.current.mode
        AppTheme(
            colorScheme = AppColorsSchemes.of(darkMode)
        ) {
            AppScreen(
                presentationModule = presentationModule,
                habitIconResourceProvider = HabitIconResourceProvider(
                    habitIconProvider = presentationModule.logicModule.habitIconProvider
                ),
                dateTimeFormatter = DateTimeFormatter(
                    dateTimeConfigProvider = presentationModule.logicModule.dateTimeConfigProvider,
                    context = this
                ),
                durationFormatter = DurationFormatter(
                    resources = resources,
                    defaultAccuracy = DurationFormatter.Accuracy.SECONDS
                ),
                dateTimeConfigProvider = presentationModule.logicModule.dateTimeConfigProvider
            )
        }
    }
}