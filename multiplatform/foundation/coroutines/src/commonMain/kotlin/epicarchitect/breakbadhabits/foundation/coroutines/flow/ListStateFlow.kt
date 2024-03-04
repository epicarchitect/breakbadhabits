package epicarchitect.breakbadhabits.foundation.coroutines.flow

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface ListStateFlow<T> : StateFlow<List<T>>, List<T>
interface MutableListStateFlow<T> : ListStateFlow<T>, MutableStateFlow<List<T>>, MutableList<T>

fun <T> MutableListStateFlow(
    initial: List<T> = emptyList()
): MutableListStateFlow<T> = ListStateFlowImpl(
    MutableStateFlow(initial)
)

private class ListStateFlowImpl<T>(
    private val state: MutableStateFlow<List<T>> = MutableStateFlow(emptyList())
) : MutableListStateFlow<T>, MutableStateFlow<List<T>> by state, MutableList<T> {
    override val size get() = value.size

    override fun clear() {
        value = emptyList()
    }

    override fun addAll(elements: Collection<T>): Boolean {
        val mutableList = value.toMutableList()
        val result = mutableList.addAll(elements)
        value = mutableList
        return result
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        val mutableList = value.toMutableList()
        val result = mutableList.addAll(index, elements)
        value = mutableList
        return result
    }

    override fun add(index: Int, element: T) {
        val mutableList = value.toMutableList()
        mutableList.add(index, element)
        value = mutableList
    }

    override fun add(element: T): Boolean {
        val mutableList = value.toMutableList()
        val result = mutableList.add(element)
        value = mutableList
        return result
    }

    override fun get(index: Int) = value[index]

    override fun isEmpty() = value.isEmpty()

    override fun iterator() = IteratorImpl(this)

    override fun listIterator() = ListIteratorImpl(this, 0)

    override fun listIterator(index: Int) = ListIteratorImpl(this, index)

    override fun removeAt(index: Int): T {
        val mutableList = value.toMutableList()
        val result = mutableList.removeAt(index)
        value = mutableList
        return result
    }

    override fun subList(fromIndex: Int, toIndex: Int) = value.toMutableList().subList(fromIndex, toIndex)

    override fun set(index: Int, element: T): T {
        val mutableList = value.toMutableList()
        val result = mutableList.set(index, element)
        value = mutableList
        return result
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        val mutableList = value.toMutableList()
        val result = mutableList.retainAll(elements)
        value = mutableList
        return result
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        val mutableList = value.toMutableList()
        val result = mutableList.removeAll(elements)
        value = mutableList
        return result
    }

    override fun remove(element: T): Boolean {
        val mutableList = value.toMutableList()
        val result = mutableList.remove(element)
        value = mutableList
        return result
    }

    override fun lastIndexOf(element: T) = value.lastIndexOf(element)

    override fun indexOf(element: T) = value.indexOf(element)

    override fun containsAll(elements: Collection<T>) = value.containsAll(elements)

    override fun contains(element: T) = value.contains(element)
}

private class IteratorImpl<T>(
    private val state: MutableListStateFlow<T>
) : MutableIterator<T> {
    private val iterator = state.value.toMutableList().iterator()
    private var currentIndex = 0
    override fun hasNext() = iterator.hasNext()

    override fun next() = iterator.next().also { currentIndex++ }

    override fun remove() {
        iterator.remove()
        state.removeAt(currentIndex)
    }
}

private class ListIteratorImpl<T>(
    private val state: MutableListStateFlow<T>,
    private val startIndex: Int
) : MutableListIterator<T> {
    private val iterator = state.value.toMutableList().let {
        if (startIndex == 0) {
            it.listIterator()
        } else {
            it.listIterator(startIndex)
        }
    }
    private var currentIndex = startIndex

    override fun add(element: T) {
        iterator.add(element)
        state.add(currentIndex, element)
    }

    override fun hasNext() = iterator.hasNext()

    override fun hasPrevious() = iterator.hasPrevious()

    override fun next() = iterator.next().also { currentIndex++ }

    override fun nextIndex() = iterator.nextIndex()

    override fun previous() = iterator.previous().also { currentIndex-- }

    override fun previousIndex() = iterator.previousIndex()

    override fun remove() {
        iterator.remove()
        state.removeAt(currentIndex)
    }

    override fun set(element: T) {
        iterator.set(element)
        state[currentIndex] = element
    }
}