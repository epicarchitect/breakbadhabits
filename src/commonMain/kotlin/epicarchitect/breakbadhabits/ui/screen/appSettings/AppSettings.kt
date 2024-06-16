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
import epicarchitect.breakbadhabits.data.AppData
import epicarchitect.breakbadhabits.data.database.appSettings.AppSettingsTheme
import epicarchitect.breakbadhabits.ui.component.FlowStateContainer
import epicarchitect.breakbadhabits.ui.component.SimpleScrollableScreen
import epicarchitect.breakbadhabits.ui.component.button.Button
import epicarchitect.breakbadhabits.ui.component.button.RadioButton
import epicarchitect.breakbadhabits.ui.component.stateOfOneOrNull
import epicarchitect.breakbadhabits.ui.component.text.InputCard
import epicarchitect.breakbadhabits.ui.screen.habits.widgets.list.HabitWidgetsScreen

class AppSettingsScreen : Screen {
    @Composable
    override fun Content() {
        AppSettings()
    }
}

@Composable
fun AppSettings() {
    val navigator = LocalNavigator.currentOrThrow
    val strings = AppData.resources.strings.appSettingsStrings
    val appSettingsQueries = AppData.database.appSettingsQueries

    FlowStateContainer(
        state = stateOfOneOrNull { appSettingsQueries.settings() }
    ) { settings ->
        SimpleScrollableScreen(
            title = strings.titleText(),
            onBackClick = navigator::pop,
        ) {
            Spacer(Modifier.height(16.dp))

            InputCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                title = "Theme",
                description = strings.themeSelectionDescription()
            ) {
                RadioButton(
                    text = strings.themeSelectionSystemTheme(),
                    selected = settings?.theme == AppSettingsTheme.SYSTEM,
                    onSelect = { appSettingsQueries.update(theme = AppSettingsTheme.SYSTEM) }
                )

                RadioButton(
                    text = strings.themeSelectionLightTheme(),
                    selected = settings?.theme == AppSettingsTheme.LIGHT,
                    onSelect = { appSettingsQueries.update(theme = AppSettingsTheme.LIGHT) }
                )

                RadioButton(
                    text = strings.themeSelectionDarkTheme(),
                    selected = settings?.theme == AppSettingsTheme.DARK,
                    onSelect = { appSettingsQueries.update(theme = AppSettingsTheme.DARK) }
                )
            }

            Spacer(Modifier.height(16.dp))

            Button(
                modifier = Modifier.padding(horizontal = 16.dp),
                onClick = { navigator += HabitWidgetsScreen() },
                text = strings.widgetsButton()
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}