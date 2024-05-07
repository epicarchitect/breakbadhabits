package epicarchitect.breakbadhabits.ui.appSettings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import epicarchitect.breakbadhabits.ui.habits.widgets.list.HabitWidgetsScreen
import epicarchitect.breakbadhabits.uikit.FlowStateContainer
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
    val resources = LocalAppSettingsResources.current

    FlowStateContainer(
        state = stateOfOneOrNull { AppData.database.appSettingsQueries.settings() }
    ) { settings ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                text = resources.titleText(),
                type = Text.Type.Title,
                priority = Text.Priority.High
            )

            Text(
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 8.dp,
                    end = 8.dp,
                    bottom = 4.dp
                ),
                text = resources.themeSelectionDescription()
            )

            RadioButton(
                text = resources.themeSelectionSystemTheme(),
                selected = settings?.theme == 0L,
                onSelect = {
                    AppData.database.appSettingsQueries.update(theme = 0L)
                }
            )

            RadioButton(
                text = resources.themeSelectionLightTheme(),
                selected = settings?.theme == 1L,
                onSelect = {
                    AppData.database.appSettingsQueries.update(theme = 1L)
                }
            )

            RadioButton(
                text = resources.themeSelectionDarkTheme(),
                selected = settings?.theme == 2L,
                onSelect = {
                    AppData.database.appSettingsQueries.update(theme = 2L)
                }
            )

            Text(
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 8.dp,
                    end = 8.dp,
                    bottom = 4.dp
                ),
                text = resources.widgetsDescription()
            )

            Button(
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 8.dp,
                    end = 8.dp,
                    bottom = 16.dp
                ),
                onClick = {
                    navigator += HabitWidgetsScreen()
                },
                text = resources.widgetsButton()
            )
        }
    }
}