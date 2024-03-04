package epicarchitect.breakbadhabits.foundation.coroutines

import epicarchitect.breakbadhabits.foundation.coroutines.flow.ListStateFlow
import epicarchitect.breakbadhabits.foundation.coroutines.flow.MutableListStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.CoroutineContext

interface CoroutineScopeOwnersManager<T : CoroutineScopeOwner> : CoroutineScopeOwner, ListStateFlow<T> {
    fun <R : T> add(
        coroutineContext: CoroutineContext = Job(),
        factory: (CoroutineScope) -> R
    ): R

    fun remove(owner: T): Boolean
    fun removeAll()
}

fun <T : CoroutineScopeOwner> CoroutineScopeOwnersManager(
    coroutineScope: CoroutineScope
): CoroutineScopeOwnersManager<T> = CoroutineScopeOwnersManagerImpl(coroutineScope)

fun <T : CoroutineScopeOwner> CoroutineScopeOwner.CoroutineScopeOwnersManager(): CoroutineScopeOwnersManager<T> =
    CoroutineScopeOwnersManagerImpl(coroutineScope)

fun <T, R : CoroutineScopeOwner> CoroutineScopeOwner.CoroutineScopeOwnersManager(
    flow: Flow<List<T>>,
    map: (CoroutineScope, T) -> R
): CoroutineScopeOwnersManager<R> = flow.asCoroutineScopeOwnersManager(coroutineScope, map)

fun <T, R : CoroutineScopeOwner> Flow<List<T>>.asCoroutineScopeOwnersManager(
    coroutineScope: CoroutineScope,
    map: (CoroutineScope, T) -> R
): CoroutineScopeOwnersManager<R> {
    val manager = CoroutineScopeOwnersManager<R>(coroutineScope)
    onEach { items ->
        manager.removeAll()
        items.forEach { item ->
            manager.add { map(it, item) }
        }
    }.launchIn(coroutineScope)
    return manager
}

private class CoroutineScopeOwnersManagerImpl<T : CoroutineScopeOwner>(
    override val coroutineScope: CoroutineScope,
    private val ownersList: MutableListStateFlow<T> = MutableListStateFlow(emptyList())
) : CoroutineScopeOwnersManager<T>, ListStateFlow<T> by ownersList {

    private val disposables = mutableMapOf<T, DisposableHandle>()

    override fun <R : T> add(
        coroutineContext: CoroutineContext,
        factory: (CoroutineScope) -> R
    ): R {
        val childScope = coroutineScope.childScope(coroutineContext)
        val owner = factory(childScope)
        ownersList.add(owner)
        disposables[owner] = childScope.invokeOnCompletion {
            remove(owner)
        }
        return owner
    }

    override fun remove(owner: T) = ownersList.remove(owner).also { isRemoved ->
        if (isRemoved) {
            disposables.remove(owner)?.dispose()
            owner.coroutineScope.cancel()
        }
    }

    override fun removeAll() {
        forEach(this::remove)
    }
}

fun <T : CoroutineScopeOwner> CoroutineScopeOwnersManager<CoroutineScopeOwner>.lazyCreate(
    coroutineContext: CoroutineContext = Job(),
    factory: (CoroutineScope) -> T
) = lazy {
    add(coroutineContext, factory)
}