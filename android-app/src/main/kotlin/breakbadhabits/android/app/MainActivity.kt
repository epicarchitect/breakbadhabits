package breakbadhabits.android.app

import androidx.compose.runtime.Composable
import breakbadhabits.android.app.format.DateTimeFormatter
import breakbadhabits.android.app.format.DurationFormatter
import breakbadhabits.android.app.ui.app.AppScreen
import breakbadhabits.android.app.ui.habits.resources.HabitIconResourceProvider
import breakbadhabits.foundation.uikit.activity.ComposeActivity

class MainActivity : ComposeActivity() {

    override val themeResourceId = R.style.Activity

    @Composable
    override fun Content() {
        val presentationModule = BreakBadHabitsApp.instance.presentationModule
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