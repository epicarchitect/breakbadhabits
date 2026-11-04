package epicarchitect.breakbadhabits.screens.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.export.ImportResult
import epicarchitect.breakbadhabits.export.exportHabitsCsv
import epicarchitect.breakbadhabits.export.rememberCsvImportLauncher
import epicarchitect.breakbadhabits.infrastructure.AnalyticsOperator
import epicarchitect.breakbadhabits.infrastructure.AppDatabase
import epicarchitect.breakbadhabits.infrastructure.Monetization
import epicarchitect.breakbadhabits.infrastructure.UrlOpener
import epicarchitect.breakbadhabits.infrastructure.appVersion
import epicarchitect.breakbadhabits.monetization.RU_STORE_APP_URL
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.export_button_description
import epicarchitect.breakbadhabits.resources.paywall_title
import epicarchitect.breakbadhabits.resources.settings_about_title
import epicarchitect.breakbadhabits.resources.settings_analytics_short
import epicarchitect.breakbadhabits.resources.settings_data_description
import epicarchitect.breakbadhabits.resources.settings_data_title
import epicarchitect.breakbadhabits.resources.settings_delete_all_data
import epicarchitect.breakbadhabits.resources.settings_delete_data_cancel
import epicarchitect.breakbadhabits.resources.settings_delete_data_confirm
import epicarchitect.breakbadhabits.resources.settings_delete_data_dialog_message
import epicarchitect.breakbadhabits.resources.settings_delete_data_dialog_title
import epicarchitect.breakbadhabits.resources.settings_import_button
import epicarchitect.breakbadhabits.resources.settings_import_dialog_ok
import epicarchitect.breakbadhabits.resources.settings_import_dialog_title
import epicarchitect.breakbadhabits.resources.settings_import_error
import epicarchitect.breakbadhabits.resources.settings_import_invalid
import epicarchitect.breakbadhabits.resources.settings_import_success
import epicarchitect.breakbadhabits.resources.settings_premium_active
import epicarchitect.breakbadhabits.resources.settings_premium_unlock
import epicarchitect.breakbadhabits.resources.settings_privacy_policy
import epicarchitect.breakbadhabits.resources.settings_rate_app
import epicarchitect.breakbadhabits.resources.settings_support
import epicarchitect.breakbadhabits.resources.back
import epicarchitect.breakbadhabits.resources.settings_title
import epicarchitect.breakbadhabits.resources.settings_version
import epicarchitect.breakbadhabits.screens.root.LocalRootBackStack
import epicarchitect.breakbadhabits.screens.root.RootRoute
import epicarchitect.breakbadhabits.screens.root.push
import epicarchitect.breakbadhabits.url.PRIVACY_POLICY_URL
import epicarchitect.breakbadhabits.url.SUPPORT_URL
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState

@Composable
fun SettingsScreen() {
    val backStack = LocalRootBackStack.current
    val coroutineScope = rememberCoroutineScope()
    var analyticsEnabled by remember {
        mutableStateOf(AnalyticsOperator.isEnabled)
    }

    val premiumCache by Monetization.premiumCache.collectAsState()
    var showDeleteDataDialog by remember {
        mutableStateOf(false)
    }
    var importResult by remember {
        mutableStateOf<ImportResult?>(null)
    }
    val importLauncher = rememberCsvImportLauncher { result ->
        importResult = result
    }
    if (showDeleteDataDialog) {
        DeleteAllDataDialog(
            onDismiss = { showDeleteDataDialog = false },
            onConfirm = {
                coroutineScope.launch {
                    AppDatabase.habitsDao.deleteAll()
                    AppDatabase.habitWidgetDao.deleteAll()
                    showDeleteDataDialog = false
                }
            }
        )
    }
    importResult?.let { result ->
        AlertDialog(
            onDismissRequest = { importResult = null },
            title = {
                Text(text = stringResource(Res.string.settings_import_dialog_title))
            },
            text = {
                Text(
                    text = when (result) {
                        is ImportResult.Success -> stringResource(
                            Res.string.settings_import_success,
                            result.habits,
                            result.events
                        )

                        ImportResult.InvalidFile -> stringResource(
                            Res.string.settings_import_invalid
                        )

                        ImportResult.Error -> stringResource(
                            Res.string.settings_import_error
                        )
                    }
                )
            },
            confirmButton = {
                TextButton(onClick = { importResult = null }) {
                    Text(text = stringResource(Res.string.settings_import_dialog_ok))
                }
            }
        )
    }

    val listState = rememberLazyListState()
    val bottomInset = WindowInsets.navigationBars
        .asPaddingValues()
        .calculateBottomPadding()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        contentPadding = PaddingValues(bottom = bottomInset),
        state = listState
    ) {
        item(key = "app_bar") {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = stringResource(Res.string.settings_title),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { backStack.removeLastOrNull() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }

        item(key = "content") {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(max = 600.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(Modifier.height(16.dp))

                    if (Monetization.purchasesAvailable) {
                        Surface(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth(),
                            onClick = { backStack.push(RootRoute.Paywall(source = "settings")) },
                            shape = MaterialTheme.shapes.extraLarge,
                            color = MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Row(
                                modifier = Modifier.padding(18.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = stringResource(Res.string.paywall_title),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = stringResource(
                                        if (premiumCache.isPremium) Res.string.settings_premium_active
                                        else Res.string.settings_premium_unlock
                                    ),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(Modifier.height(16.dp))
                    }

                    Spacer(Modifier.height(16.dp))

                    SettingsSectionCard(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        title = stringResource(Res.string.settings_data_title)
                    ) {
                        Text(
                            text = stringResource(Res.string.settings_data_description),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(Modifier.height(8.dp))

                        TextButton(
                            onClick = {
                                coroutineScope.launch {
                                    runCatching { exportHabitsCsv() }
                                }
                            }
                        ) {
                            Text(stringResource(Res.string.export_button_description))
                        }

                        TextButton(
                            onClick = {
                                importLauncher.launch()
                            }
                        ) {
                            Text(stringResource(Res.string.settings_import_button))
                        }

                        TextButton(
                            onClick = { showDeleteDataDialog = true }
                        ) {
                            Text(
                                text = stringResource(Res.string.settings_delete_all_data),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    SettingsSectionCard(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        title = stringResource(Res.string.settings_about_title)
                    ) {
                        Text(
                            text = stringResource(Res.string.settings_version, appVersion),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = stringResource(Res.string.settings_privacy_policy),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable {
                                UrlOpener.openUrl(PRIVACY_POLICY_URL)
                            }
                        )

                        Spacer(Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = stringResource(Res.string.settings_analytics_short),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Switch(
                                checked = analyticsEnabled,
                                onCheckedChange = {
                                    analyticsEnabled = it
                                    AnalyticsOperator.isEnabled = it
                                }
                            )
                        }

                        Spacer(Modifier.height(8.dp))

                        TextButton(
                            onClick = { UrlOpener.openUrl(SUPPORT_URL) }
                        ) {
                            Text(stringResource(Res.string.settings_support))
                        }

                        TextButton(
                            onClick = { UrlOpener.openUrl(RU_STORE_APP_URL) }
                        ) {
                            Text(stringResource(Res.string.settings_rate_app))
                        }

                    }

                    Spacer(Modifier.height(16.dp))
    
                }
            }
        }
    }
}

@Composable
private fun DeleteAllDataDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(Res.string.settings_delete_data_dialog_title))
        },
        text = {
            Text(text = stringResource(Res.string.settings_delete_data_dialog_message))
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(Res.string.settings_delete_data_confirm),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(Res.string.settings_delete_data_cancel))
            }
        }
    )
}

@Composable
private fun SettingsSectionCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f))
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(8.dp))

            content()
        }
    }
}
