package breakbadhabits.foundation.math.ranges

fun <T : Comparable<T>> List<ClosedRange<T>>.normalized(): List<ClosedRange<T>> {
    if (size < 2) return this

    val mutableList = toSet().sortedBy(ClosedRange<T>::start).toMutableList()

    var checkingItemIndex = 0
    var isNormalized = false
    while (isNormalized.not()) {
        isNormalized = true
        val checkingItem = mutableList[checkingItemIndex]
        var checkingItem2Index = 0
        while (
            checkingItem2Index < mutableList.size && mutableList.find {
                it.valuesAreEquals(checkingItem)
            } != null
        ) {
            if (checkingItemIndex == checkingItem2Index) {
                checkingItem2Index++
                continue
            }

            val checkingItem2 = mutableList[checkingItem2Index]

            if (checkingItem.valuesAreEquals(checkingItem2)) {
                mutableList.removeAll { it.valuesAreEquals(checkingItem2) }
                mutableList.add(checkingItemIndex, checkingItem2)
                isNormalized = false
                break
            }

            val result = combineOrNull(checkingItem, checkingItem2)

            if (result != null) {
                mutableList.removeAll { it.valuesAreEquals(checkingItem) }
                mutableList.removeAll { it.valuesAreEquals(checkingItem2) }
                mutableList.add(0, result)
                checkingItem2Index = 0
                isNormalized = false
            } else {
                checkingItem2Index++
            }
        }
        if (isNormalized) {
            if (checkingItemIndex < mutableList.size - 1) {
                isNormalized = false
            }
            checkingItemIndex++
        } else {
            checkingItemIndex = 0
        }
    }

    return mutableList.map(ClosedRange<T>::ascended).sortedBy(ClosedRange<T>::start)
}