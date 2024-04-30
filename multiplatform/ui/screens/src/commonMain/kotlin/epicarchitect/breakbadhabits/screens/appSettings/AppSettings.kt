package epicarchitect.breakbadhabits.screens.appSettings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.database.AppData
import epicarchitect.breakbadhabits.foundation.uikit.button.Button
import epicarchitect.breakbadhabits.foundation.uikit.button.RadioButton
import epicarchitect.breakbadhabits.foundation.uikit.text.Text
import epicarchitect.breakbadhabits.screens.habits.widgets.list.HabitWidgetsScreen
import kotlinx.coroutines.Dispatchers

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
    val appSettingsState = remember {
        AppData.mainDatabase
            .appSettingsQueries
            .get()
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
    }.collectAsState(initial = null)

    val appSettings = appSettingsState.value

    if (appSettings != null) {
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
                selected = appSettings.theme == 0L,
                onSelect = {
                    AppData.mainDatabase.appSettingsQueries.update(theme = 0L)
                }
            )

            RadioButton(
                text = resources.themeSelectionLightTheme(),
                selected = appSettings.theme == 1L,
                onSelect = {
                    AppData.mainDatabase.appSettingsQueries.update(theme = 1L)
                }
            )

            RadioButton(
                text = resources.themeSelectionDarkTheme(),
                selected = appSettings.theme == 2L,
                onSelect = {
                    AppData.mainDatabase.appSettingsQueries.update(theme = 2L)
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