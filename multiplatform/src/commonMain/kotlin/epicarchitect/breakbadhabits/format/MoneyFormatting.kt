package epicarchitect.breakbadhabits.format

import kotlin.math.absoluteValue

fun formatPriceMinorUnits(minorUnits: Int, currencyCode: String): String {
    val sign = if (minorUnits < 0) "-" else ""
    val absoluteMinorUnits = minorUnits.absoluteValue
    val majorUnits = absoluteMinorUnits / 100
    val kopecks = absoluteMinorUnits % 100
    val amount = if (kopecks == 0) {
        majorUnits.formatted()
    } else {
        "${majorUnits.formatted()},${kopecks.toString().padStart(2, '0')}"
    }

    return sign + amount.withCurrencySymbol(currencyCode)
}

fun formatPriceMajorUnits(majorUnits: Int, currencyCode: String): String =
    majorUnits.formatted().withCurrencySymbol(currencyCode)

fun currencySymbol(currencyCode: String): String = when (currencyCode.trim().uppercase()) {
    "RUB" -> "₽"
    "USD" -> "$"
    "EUR" -> "€"
    "GBP" -> "£"
    "KZT" -> "₸"
    "BYN" -> "Br"
    "UAH" -> "₴"
    "GEL" -> "₾"
    "TRY" -> "₺"
    "AMD" -> "֏"
    "AZN" -> "₼"
    else -> currencyCode.trim().uppercase()
}

private fun String.withCurrencySymbol(currencyCode: String): String =
    currencySymbol(currencyCode).takeIf { it.isNotEmpty() }?.let { "$this $it" } ?: this
