package epicarchitect.breakbadhabits.ui.appSettings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.data.AppData
import epicarchitect.breakbadhabits.data.database.appSettings.AppSettingsTheme
import epicarchitect.breakbadhabits.ui.habits.widgets.list.HabitWidgetsScreen
import epicarchitect.breakbadhabits.uikit.FlowStateContainer
import epicarchitect.breakbadhabits.uikit.SimpleTopAppBar
import epicarchitect.breakbadhabits.uikit.button.Button
import epicarchitect.breakbadhabits.uikit.button.RadioButton
import epicarchitect.breakbadhabits.uikit.stateOfOneOrNull
import epicarchitect.breakbadhabits.uikit.text.Text

class AppSettingsScreen : Screen {
    @Composable
    override fun Content() {
        AppSettings()
    }
}

@Composable
fun AppSettings() {
    val navigator = LocalNavigator.currentOrThrow
    val appSettingsStrings = AppData.resources.strings.appSettingsStrings
    val appSettingsQueries = AppData.database.appSettingsQueries

    FlowStateContainer(
        state = stateOfOneOrNull { appSettingsQueries.settings() }
    ) { settings ->
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
        ) {
            SimpleTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = appSettingsStrings.titleText(),
                onBackClick = navigator::pop,
            )

            Text(
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 8.dp,
                    end = 8.dp,
                    bottom = 4.dp
                ),
                text = appSettingsStrings.themeSelectionDescription()
            )

            RadioButton(
                text = appSettingsStrings.themeSelectionSystemTheme(),
                selected = settings?.theme == AppSettingsTheme.SYSTEM,
                onSelect = { appSettingsQueries.update(theme = AppSettingsTheme.SYSTEM) }
            )

            RadioButton(
                text = appSettingsStrings.themeSelectionLightTheme(),
                selected = settings?.theme == AppSettingsTheme.LIGHT,
                onSelect = { appSettingsQueries.update(theme = AppSettingsTheme.LIGHT) }
            )

            RadioButton(
                text = appSettingsStrings.themeSelectionDarkTheme(),
                selected = settings?.theme == AppSettingsTheme.DARK,
                onSelect = { appSettingsQueries.update(theme = AppSettingsTheme.DARK) }
            )

            Text(
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 8.dp,
                    end = 8.dp,
                    bottom = 4.dp
                ),
                text = appSettingsStrings.widgetsDescription()
            )

            Button(
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 8.dp,
                    end = 8.dp,
                    bottom = 16.dp
                ),
                onClick = { navigator += HabitWidgetsScreen() },
                text = appSettingsStrings.widgetsButton()
            )
        }
    }
}