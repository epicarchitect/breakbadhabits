package epicarchitect.breakbadhabits.export

import androidx.compose.runtime.Composable

internal class BackupImportLauncher internal constructor(
    private val launchImport: () -> Unit
) {
    fun launch() = launchImport()
}

@Composable
internal expect fun rememberBackupImportLauncher(
    onResult: (BackupResult) -> Unit
): BackupImportLauncher
