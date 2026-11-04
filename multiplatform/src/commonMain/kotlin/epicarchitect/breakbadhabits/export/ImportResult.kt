package epicarchitect.breakbadhabits.export

sealed interface ImportResult {
    data class Success(
        val habits: Int,
        val events: Int
    ) : ImportResult

    data object InvalidFile : ImportResult
    data object Error : ImportResult
}
