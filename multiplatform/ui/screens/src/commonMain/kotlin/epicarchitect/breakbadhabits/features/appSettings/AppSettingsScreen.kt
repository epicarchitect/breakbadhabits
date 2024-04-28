package epicarchitect.breakbadhabits.features.appSettings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import epicarchitect.breakbadhabits.foundation.uikit.button.Button
import epicarchitect.breakbadhabits.foundation.uikit.button.RadioButton
import epicarchitect.breakbadhabits.foundation.uikit.text.Text
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AppSettings(dependencies: AppSettingsDependencies) {
    val coroutineScope = rememberCoroutineScope(Dispatchers::IO)
    val appSettingsState = dependencies.mainDatabase
        .appSettingsQueries
        .get()
        .asFlow()
        .mapToOneOrNull(Dispatchers.IO)
        .collectAsState(initial = null)

    val appSettings = appSettingsState.value

    if (appSettings != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp),
                text = dependencies.resources.titleText(),
                type = Text.Type.Title,
                priority = Text.Priority.High
            )

            Text(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        top = 8.dp,
                        end = 8.dp,
                        bottom = 4.dp
                    ),
                text = dependencies.resources.themeSelectionDescription()
            )

            RadioButton(
                text = dependencies.resources.themeSelectionSystemTheme(),
                selected = appSettings.theme == 0L,
                onSelect = {
                    coroutineScope.launch {
                        dependencies.mainDatabase.appSettingsQueries.update(theme = 0L)
                    }
                }
            )

            RadioButton(
                text = dependencies.resources.themeSelectionLightTheme(),
                selected = appSettings.theme == 1L,
                onSelect = {
                    coroutineScope.launch {
                        dependencies.mainDatabase.appSettingsQueries.update(theme = 1L)
                    }
                }
            )

            RadioButton(
                text = dependencies.resources.themeSelectionDarkTheme(),
                selected = appSettings.theme == 2L,
                onSelect = {
                    coroutineScope.launch {
                        dependencies.mainDatabase.appSettingsQueries.update(theme = 2L)
                    }
                }
            )

            Text(
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 8.dp,
                    end = 8.dp,
                    bottom = 4.dp
                ),
                text = dependencies.resources.widgetsDescription()
            )

            Button(
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 8.dp,
                    end = 8.dp,
                    bottom = 16.dp
                ),
                onClick = dependencies.navigation::openAppWidgets,
                text = dependencies.resources.widgetsButton()
            )
        }
    }
}