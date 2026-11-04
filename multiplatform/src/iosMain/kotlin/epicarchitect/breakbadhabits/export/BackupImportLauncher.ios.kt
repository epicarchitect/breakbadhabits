package epicarchitect.breakbadhabits.export

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState

@Composable
internal actual fun rememberBackupImportLauncher(
    onResult: (BackupResult) -> Unit
): BackupImportLauncher {
    val resultCallback = rememberUpdatedState(onResult)
    val importer = remember { IosBackupImporter() }

    return remember(importer) {
        BackupImportLauncher {
            importer.importData { result ->
                resultCallback.value(result)
            }
        }
    }
}
