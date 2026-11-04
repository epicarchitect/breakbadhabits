package epicarchitect.breakbadhabits.screens.habits.details

import kotlin.test.Test
import kotlin.test.assertEquals

class HabitRiskMapSummaryTest {

    private val russian = RiskMapSummaryLabels(
        prefix = "За последние 90 дней срывы чаще всего происходили",
        zonePattern = "по %1\$s %2\$s",
        conjunction = "и",
        dayNames = listOf(
            "понедельникам",
            "вторникам",
            "средам",
            "четвергам",
            "пятницам",
            "субботам",
            "воскресеньям"
        ),
        periodNames = listOf("утром", "днём", "вечером", "ночью")
    )

    private val english = RiskMapSummaryLabels(
        prefix = "Over the last 90 days, slips most often happened",
        zonePattern = "on %1\$s %2\$s",
        conjunction = "and",
        dayNames = listOf(
            "Mondays",
            "Tuesdays",
            "Wednesdays",
            "Thursdays",
            "Fridays",
            "Saturdays",
            "Sundays"
        ),
        periodNames = listOf(
            "in the morning",
            "in the afternoon",
            "in the evening",
            "at night"
        )
    )

    @Test
    fun russianGroupsDaysAndPeriods() {
        val riskMap = riskMap(
            8 to 2,
            16 to 3,
            18 to 4,
            27 to 2
        )

        assertEquals(
            "За последние 90 дней срывы чаще всего происходили " +
                "по вторникам днём, по средам и пятницам вечером " +
                "и по воскресеньям ночью.",
            buildRiskMapSummary(riskMap, russian)
        )
    }

    @Test
    fun englishGroupsDaysAndPeriods() {
        val riskMap = riskMap(
            8 to 2,
            16 to 3,
            18 to 4,
            27 to 2
        )

        assertEquals(
            "Over the last 90 days, slips most often happened " +
                "on Tuesdays in the afternoon, on Wednesdays and Fridays in the evening " +
                "and on Sundays at night.",
            buildRiskMapSummary(riskMap, english)
        )
    }

    @Test
    fun singleZoneHasNoConjunction() {
        val riskMap = riskMap(18 to 2)

        assertEquals(
            "За последние 90 дней срывы чаще всего происходили по пятницам вечером.",
            buildRiskMapSummary(riskMap, russian)
        )
        assertEquals(
            "Over the last 90 days, slips most often happened on Fridays in the evening.",
            buildRiskMapSummary(riskMap, english)
        )
    }


    @Test
    fun twoPeriodGroupsUseConjunction() {
        val riskMap = riskMap(0 to 2, 18 to 2)

        assertEquals(
            "За последние 90 дней срывы чаще всего происходили " +
                "по понедельникам утром и по пятницам вечером.",
            buildRiskMapSummary(riskMap, russian)
        )
        assertEquals(
            "Over the last 90 days, slips most often happened " +
                "on Mondays in the morning and on Fridays in the evening.",
            buildRiskMapSummary(riskMap, english)
        )
    }

    @Test
    fun supportsAllFourPeriodGroups() {
        val riskMap = riskMap(0 to 2, 8 to 2, 16 to 2, 24 to 2)

        assertEquals(
            "За последние 90 дней срывы чаще всего происходили " +
                "по понедельникам утром, по вторникам днём, " +
                "по средам вечером и по четвергам ночью.",
            buildRiskMapSummary(riskMap, russian)
        )
        assertEquals(
            "Over the last 90 days, slips most often happened " +
                "on Mondays in the morning, on Tuesdays in the afternoon, " +
                "on Wednesdays in the evening and on Thursdays at night.",
            buildRiskMapSummary(riskMap, english)
        )
    }

    @Test
    fun supportsFiveDaysInOnePeriod() {
        val riskMap = riskMap(
            14 to 2,
            15 to 2,
            16 to 2,
            17 to 2,
            18 to 2
        )

        assertEquals(
            "За последние 90 дней срывы чаще всего происходили " +
                "по понедельникам, вторникам, средам, четвергам и пятницам вечером.",
            buildRiskMapSummary(riskMap, russian)
        )
        assertEquals(
            "Over the last 90 days, slips most often happened " +
                "on Mondays, Tuesdays, Wednesdays, Thursdays and Fridays in the evening.",
            buildRiskMapSummary(riskMap, english)
        )
    }

    @Test
    fun sameDayAcrossAllPeriodsNamesDayOnce() {
        val riskMap = riskMap(0 to 2, 7 to 2, 14 to 2, 21 to 2)

        assertEquals(
            "За последние 90 дней срывы чаще всего происходили " +
                "по понедельникам утром, днём, вечером и ночью.",
            buildRiskMapSummary(riskMap, russian)
        )
        assertEquals(
            "Over the last 90 days, slips most often happened " +
                "on Mondays in the morning, in the afternoon, " +
                "in the evening and at night.",
            buildRiskMapSummary(riskMap, english)
        )
    }

    @Test
    fun highlightsOnlyDaysAndPeriods() {
        val text = buildRiskMapSummary(riskMap(18 to 2), russian)

        assertEquals(
            listOf("пятницам", "вечером"),
            riskMapSummaryHighlightRanges(text, russian).map { text.substring(it) }
        )
    }

    private fun riskMap(vararg cells: Pair<Int, Int>) = HabitRiskMapData(
        highlightedCells = cells.toMap(),
        maxCount = cells.maxOf { it.second }
    )
}
