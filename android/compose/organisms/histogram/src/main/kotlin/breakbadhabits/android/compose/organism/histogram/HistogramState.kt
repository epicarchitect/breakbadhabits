package breakbadhabits.android.compose.organism.histogram

data class HistogramState(
    val items: List<Item>
) {
    data class Item(
        val id: Int,
        val value: Float,
        val formattedValue: String
    )
}