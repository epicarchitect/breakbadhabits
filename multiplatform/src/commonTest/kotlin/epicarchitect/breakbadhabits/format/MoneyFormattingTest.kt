package epicarchitect.breakbadhabits.format

import kotlin.test.Test
import kotlin.test.assertEquals

class MoneyFormattingTest {

    @Test
    fun wholeRublesHideKopecksAndUseSymbol() {
        assertEquals("2 990 ₽", formatPriceMinorUnits(299_000, "RUB"))
    }

    @Test
    fun nonZeroKopecksStayVisible() {
        assertEquals("2 990,50 ₽", formatPriceMinorUnits(299_050, "RUB"))
    }

    @Test
    fun majorUnitsAreFormattedWithoutKopecks() {
        assertEquals("249 ₽", formatPriceMajorUnits(249, "RUB"))
    }

    @Test
    fun knownCurrenciesUseSymbols() {
        assertEquals("2 990 $", formatPriceMinorUnits(299_000, "USD"))
        assertEquals("2 990 €", formatPriceMinorUnits(299_000, "EUR"))
        assertEquals("2 990 ₸", formatPriceMinorUnits(299_000, "KZT"))
    }

    @Test
    fun unknownCurrencyFallsBackToCode() {
        assertEquals("2 990 XYZ", formatPriceMinorUnits(299_000, "xyz"))
    }

    @Test
    fun blankCurrencyKeepsOnlyAmount() {
        assertEquals("2 990", formatPriceMinorUnits(299_000, ""))
    }
}
