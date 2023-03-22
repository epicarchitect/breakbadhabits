package breakbadhabits.foundation.math.ranges

/**
 * [1..3, 3..6, 8..10, 7..9] -> [1..6, 7..10]
 * */
fun <T : Comparable<T>> Iterable<ClosedRange<T>>.combineIntersections(): List<ClosedRange<T>> {
    val mutableList = toSet().sortedBy(ClosedRange<T>::start).toMutableList()

    if (mutableList.size < 2) return mutableList.toList()

    var currentItem1Index = 0
    var isNormalized = false

    while (isNormalized.not()) {
        isNormalized = true
        val currentItem1 = mutableList[currentItem1Index]
        var currentItem2Index = 0

        while (
            currentItem2Index < mutableList.size && mutableList.find {
                valuesAreEqual(it, currentItem1)
            } != null
        ) {
            if (currentItem1Index == currentItem2Index) {
                currentItem2Index++
                continue
            }

            val currentItem2 = mutableList[currentItem2Index]

            if (valuesAreEqual(currentItem1, currentItem2)) {
                mutableList.removeAll { valuesAreEqual(it, currentItem2) }
                mutableList.add(currentItem1Index, currentItem2)
                isNormalized = false
                break
            }

            val result = combineOrNull(currentItem1, currentItem2)

            if (result != null) {
                mutableList.removeAll { valuesAreEqual(it, currentItem1) }
                mutableList.removeAll { valuesAreEqual(it, currentItem2) }
                mutableList.add(0, result)
                currentItem2Index = 0
                isNormalized = false
            } else {
                currentItem2Index++
            }
        }

        if (isNormalized) {
            if (currentItem1Index < mutableList.size - 1) {
                isNormalized = false
            }
            currentItem1Index++
        } else {
            currentItem1Index = 0
        }
    }

    return mutableList.map(ClosedRange<T>::ascended).sortedBy(ClosedRange<T>::start)
}