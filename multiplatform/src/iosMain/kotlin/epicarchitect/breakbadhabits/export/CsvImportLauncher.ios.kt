package epicarchitect.breakbadhabits.export

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState

@Composable
actual fun rememberCsvImportLauncher(
    onResult: (ImportResult) -> Unit
): CsvImportLauncher {
    val resultCallback = rememberUpdatedState(onResult)
    val importer = remember { IosDataImporter() }

    return remember(importer) {
        CsvImportLauncher {
            importer.importData { result ->
                resultCallback.value(result)
            }
        }
    }
}
