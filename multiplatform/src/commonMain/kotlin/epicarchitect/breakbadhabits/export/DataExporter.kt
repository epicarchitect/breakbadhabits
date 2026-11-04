package epicarchitect.breakbadhabits.export

interface DataExporter {
    suspend fun exportCsv(
        fileName: String,
        content: ByteArray
    )
}
