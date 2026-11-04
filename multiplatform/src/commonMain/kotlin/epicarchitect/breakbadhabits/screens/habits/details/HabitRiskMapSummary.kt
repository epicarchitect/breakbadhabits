package epicarchitect.breakbadhabits.screens.habits.details

data class RiskMapSummaryLabels(
    val prefix: String,
    val zonePattern: String,
    val conjunction: String,
    val dayNames: List<String>,
    val periodNames: List<String>
)

fun buildRiskMapSummary(
    riskMap: HabitRiskMapData,
    labels: RiskMapSummaryLabels
): String {
    val cells = riskMap.highlightedCells.keys
    val periodGroups = cells
        .groupBy { it / 7 }
        .entries
        .sortedBy { it.key }
        .map { (period, indices) ->
            period to indices.map { it % 7 }.distinct().sorted()
        }
    val dayGroups = cells
        .groupBy { it % 7 }
        .entries
        .sortedBy { it.key }
        .map { (day, indices) ->
            day to indices.map { it / 7 }.distinct().sorted()
        }

    val groupTexts = if (periodGroups.size <= dayGroups.size) {
        periodGroups.map { (period, days) ->
            val dayText = joinLocalized(days.map { labels.dayNames[it] }, labels.conjunction)
            labels.zonePattern
                .replace("%1\$s", dayText)
                .replace("%2\$s", labels.periodNames[period])
        }
    } else {
        dayGroups.map { (day, periods) ->
            val periodText = joinLocalized(
                periods.map { labels.periodNames[it] },
                labels.conjunction
            )
            labels.zonePattern
                .replace("%1\$s", labels.dayNames[day])
                .replace("%2\$s", periodText)
        }
    }

    return "${labels.prefix} ${joinLocalized(groupTexts, labels.conjunction)}."
}

fun riskMapSummaryHighlightRanges(
    text: String,
    labels: RiskMapSummaryLabels
): List<IntRange> {
    val ranges = mutableListOf<IntRange>()
    (labels.dayNames + labels.periodNames)
        .distinct()
        .forEach { word ->
            var start = text.indexOf(word)
            while (start >= 0) {
                ranges += start until start + word.length
                start = text.indexOf(word, start + word.length)
            }
        }
    return ranges.sortedBy { it.first }
}

private fun joinLocalized(items: List<String>, conjunction: String): String = when (items.size) {
    0 -> ""
    1 -> items.first()
    2 -> "${items[0]} $conjunction ${items[1]}"
    else -> items.dropLast(1).joinToString(", ") + " $conjunction " + items.last()
}
