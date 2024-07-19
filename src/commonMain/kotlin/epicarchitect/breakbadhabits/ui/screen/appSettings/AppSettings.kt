package epicarchitect.breakbadhabits.ui.screen.appSettings

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.environment.Environment
import epicarchitect.breakbadhabits.database.AppSettings
import epicarchitect.breakbadhabits.environment.database.AppSettingsTheme
import epicarchitect.breakbadhabits.ui.component.FlowStateContainer
import epicarchitect.breakbadhabits.ui.component.SimpleScrollableScreen
import epicarchitect.breakbadhabits.ui.component.button.Button
import epicarchitect.breakbadhabits.ui.component.button.RadioButton
import epicarchitect.breakbadhabits.ui.component.stateOfOneOrNull
import epicarchitect.breakbadhabits.ui.component.text.InputCard
import epicarchitect.breakbadhabits.ui.screen.habits.widgets.dashboard.HabitWidgetsDashboardScreen

class AppSettingsScreen : Screen {
    @Composable
    override fun Content() {
        AppSettings()
    }
}

@Composable
fun AppSettings() {
    val navigator = LocalNavigator.currentOrThrow
    val strings = Environment.resources.strings.appSettingsStrings
    val appSettingsQueries = Environment.database.appSettingsQueries

    FlowStateContainer(
        state = stateOfOneOrNull { appSettingsQueries.settings() }
    ) { settings ->
        SimpleScrollableScreen(
            title = strings.titleText(),
            onBackClick = navigator::pop,
        ) {
            if (settings != null) {
                Loaded(settings)
            }
        }
    }
}

@Composable
private fun Loaded(settings: AppSettings) {
    val navigator = LocalNavigator.currentOrThrow
    val strings = Environment.resources.strings.appSettingsStrings
    val appSettingsQueries = Environment.database.appSettingsQueries

    Spacer(Modifier.height(16.dp))

    InputCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        title = strings.themeTitle(),
        description = strings.themeDescription()
    ) {
        RadioButton(
            text = strings.themeSystem(),
            selected = settings.theme == AppSettingsTheme.SYSTEM,
            onSelect = {
                appSettingsQueries.update(
                    theme = AppSettingsTheme.SYSTEM
                )
            }
        )

        RadioButton(
            text = strings.themeLight(),
            selected = settings.theme == AppSettingsTheme.LIGHT,
            onSelect = {
                appSettingsQueries.update(
                    theme = AppSettingsTheme.LIGHT
                )
            }
        )

        RadioButton(
            text = strings.themeDark(),
            selected = settings.theme == AppSettingsTheme.DARK,
            onSelect = {
                appSettingsQueries.update(
                    theme = AppSettingsTheme.DARK
                )
            }
        )
    }

    Spacer(Modifier.height(16.dp))

    Button(
        modifier = Modifier.padding(horizontal = 16.dp),
        onClick = { navigator += HabitWidgetsDashboardScreen() },
        text = strings.widgetsButton()
    )

    Spacer(Modifier.height(16.dp))
}