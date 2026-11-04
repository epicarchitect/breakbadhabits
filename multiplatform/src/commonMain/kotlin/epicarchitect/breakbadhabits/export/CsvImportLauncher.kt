package epicarchitect.breakbadhabits.export

import androidx.compose.runtime.Composable

class CsvImportLauncher internal constructor(
    private val launchImport: () -> Unit
) {
    fun launch() = launchImport()
}

@Composable
expect fun rememberCsvImportLauncher(
    onResult: (ImportResult) -> Unit
): CsvImportLauncher
