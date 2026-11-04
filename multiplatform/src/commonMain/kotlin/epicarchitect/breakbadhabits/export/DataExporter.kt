package epicarchitect.breakbadhabits.export

interface DataExporter {
    suspend fun exportBackup(
        fileName: String,
        content: ByteArray
    )
}
