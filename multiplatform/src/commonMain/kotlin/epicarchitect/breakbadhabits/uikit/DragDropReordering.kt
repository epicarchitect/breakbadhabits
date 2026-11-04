package epicarchitect.breakbadhabits.uikit

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Immutable
class DragDropMotion(
    val scheme: MotionScheme = MotionScheme.expressive(),
    val draggingScale: Float = 1.025f,
    val restingScale: Float = 1f,
) {
    companion object {
        val Default: DragDropMotion = DragDropMotion()
    }
}

@Composable
fun rememberDragDropMotion(
    scheme: MotionScheme = MaterialTheme.motionScheme,
    draggingScale: Float = 1.025f,
    restingScale: Float = 1f,
): DragDropMotion = remember(scheme, draggingScale, restingScale) {
    DragDropMotion(
        scheme = scheme,
        draggingScale = draggingScale,
        restingScale = restingScale,
    )
}

@Composable
fun Modifier.dragDropVisuals(
    isDragging: Boolean,
    motion: DragDropMotion = DragDropMotion.Default,
): Modifier {
    val scale by animateFloatAsState(
        targetValue = if (isDragging) motion.draggingScale else motion.restingScale,
        animationSpec = motion.scheme.defaultSpatialSpec(),
        label = "dragDropScale",
    )
    return graphicsLayer {
        scaleX = scale
        scaleY = scale
    }
}

fun <T> List<T>.reordered(fromIndex: Int, toIndex: Int): List<T> {
    if (fromIndex == toIndex) return this
    return toMutableList().apply { add(toIndex, removeAt(fromIndex)) }
}

@Stable
class DragDropState internal constructor(
    private val lazyListState: LazyListState?,
    private val scrollState: ScrollState?,
    val motion: DragDropMotion,
    private val scope: CoroutineScope,
    private val scrollThresholdPx: Float,
    private val maxScrollStepPx: Float,
    private val onMove: (fromIndex: Int, toIndex: Int) -> Unit,
) {
    private class ItemInfo(
        val index: Int,
        val top: Float,
        val height: Int,
        val placement: MutableFloatState,
    )

    private val items = mutableStateMapOf<Any, ItemInfo>()
    private val placementJobs = mutableMapOf<Any, Job>()

    private var containerTop = 0f
    private var containerBottom = 0f

    private var draggingKey by mutableStateOf<Any?>(null)
    private var settlingKey by mutableStateOf<Any?>(null)
    private var settleOffset by mutableFloatStateOf(0f)

    private var dragInitialTop by mutableFloatStateOf(0f)
    private var dragHeight by mutableIntStateOf(0)
    private var dragDelta by mutableFloatStateOf(0f)
    private var pendingTargetIndex: Int? = null
    private var autoScrollJob: Job? = null
    private var settleJob: Job? = null

    val isAnyItemDragging: Boolean
        get() = draggingKey != null

    fun isDragging(key: Any): Boolean = draggingKey == key

    fun isSettling(key: Any): Boolean = settlingKey == key

    fun isActive(key: Any): Boolean = draggingKey == key || settlingKey == key

    internal fun registerContainer(top: Float, height: Int) {
        containerTop = top
        containerBottom = top + height
    }

    internal fun registerItem(key: Any, index: Int, top: Float, height: Int) {
        val previous = items[key]
        if (
            previous != null &&
            previous.index == index &&
            previous.top == top &&
            previous.height == height
        ) {
            return
        }

        val placement = previous?.placement ?: mutableFloatStateOf(0f)

        if (
            previous != null &&
            previous.index != index &&
            key != draggingKey &&
            key != settlingKey
        ) {
            val delta = (previous.top + placement.floatValue) - top
            if (delta != 0f) {
                placementJobs.remove(key)?.cancel()
                placement.floatValue = delta
                placementJobs[key] = scope.launch {
                    animate(
                        initialValue = delta,
                        targetValue = 0f,
                        animationSpec = motion.scheme.defaultSpatialSpec(),
                    ) { value, _ ->
                        placement.floatValue = value
                    }
                }
            }
        }

        items[key] = ItemInfo(
            index = index,
            top = top,
            height = height,
            placement = placement,
        )
    }

    internal fun unregisterItem(key: Any) {
        if (key != draggingKey && key != settlingKey) {
            placementJobs.remove(key)?.cancel()
            items.remove(key)
        }
    }

    internal fun startDrag(key: Any) {
        if (draggingKey != null) return
        val info = items[key] ?: return

        settleJob?.cancel()
        settleJob = null
        settlingKey = null
        placementJobs.remove(key)?.cancel()

        draggingKey = key
        dragInitialTop = info.top + info.placement.floatValue
        dragHeight = info.height
        dragDelta = 0f
        pendingTargetIndex = null

        startAutoScroll()
    }

    internal fun drag(delta: Float) {
        if (draggingKey == null) return
        dragDelta += delta
        updateTarget()
    }

    internal fun stopDrag() {
        val key = draggingKey ?: return
        autoScrollJob?.cancel()
        autoScrollJob = null

        val translation = translationFor(key)

        draggingKey = null
        dragInitialTop = 0f
        dragHeight = 0
        dragDelta = 0f
        pendingTargetIndex = null

        settleOffset = translation
        settlingKey = key
        settleJob?.cancel()
        settleJob = scope.launch {
            animate(
                initialValue = translation,
                targetValue = 0f,
                animationSpec = motion.scheme.defaultSpatialSpec(),
            ) { value, _ ->
                settleOffset = value
            }
            placementJobs.remove(key)?.cancel()
            items[key]?.placement?.floatValue = 0f
            if (settlingKey == key) {
                settlingKey = null
            }
        }
    }

    internal fun translationFor(key: Any): Float {
        if (key == draggingKey) {
            val top = items[key]?.top ?: return 0f
            return dragInitialTop + dragDelta - top
        }
        if (key == settlingKey) {
            return settleOffset
        }
        return items[key]?.placement?.floatValue ?: 0f
    }

    internal fun zIndexFor(key: Any): Float = if (isActive(key)) 1f else 0f

    private fun startAutoScroll() {
        autoScrollJob?.cancel()
        autoScrollJob = scope.launch {
            while (true) {
                autoScrollStep()
                withFrameNanos { }
            }
        }
    }

    private suspend fun autoScrollStep() {
        val key = draggingKey ?: return
        val info = items[key] ?: return
        if (containerBottom <= containerTop) return

        val visualTop = dragInitialTop + dragDelta
        val visualBottom = visualTop + info.height
        val topEdge = containerTop + scrollThresholdPx
        val bottomEdge = containerBottom - scrollThresholdPx

        val step = when {
            visualTop < topEdge && canScrollBackward() ->
                -(topEdge - visualTop).coerceAtMost(maxScrollStepPx)

            visualBottom > bottomEdge && canScrollForward() ->
                (visualBottom - bottomEdge).coerceAtMost(maxScrollStepPx)

            else -> 0f
        }

        if (step != 0f) {
            scrollBy(step)
            updateTarget()
        }
    }

    private fun canScrollBackward(): Boolean {
        return lazyListState?.canScrollBackward
            ?: scrollState?.let { it.value > 0 }
            ?: false
    }

    private fun canScrollForward(): Boolean {
        return lazyListState?.canScrollForward
            ?: scrollState?.let { it.value < it.maxValue }
            ?: false
    }

    private suspend fun scrollBy(delta: Float) {
        lazyListState?.scrollBy(delta) ?: scrollState?.scrollBy(delta)
    }

    private fun updateTarget() {
        val key = draggingKey ?: return
        val fromInfo = items[key] ?: return

        val pending = pendingTargetIndex
        if (pending != null) {
            if (fromInfo.index == pending) {
                pendingTargetIndex = null
            } else {
                return
            }
        }

        val center = dragInitialTop + dragDelta + dragHeight / 2f
        val targetEntry = items.entries.firstOrNull { (itemKey, info) ->
            itemKey != key && center >= info.top && center < info.top + info.height
        } ?: return

        val targetIndex = targetEntry.value.index
        if (targetIndex == fromInfo.index) return

        val lazy = lazyListState
        if (
            lazy != null &&
            (fromInfo.index == lazy.firstVisibleItemIndex ||
                    targetIndex == lazy.firstVisibleItemIndex)
        ) {
            lazy.requestScrollToItem(
                lazy.firstVisibleItemIndex,
                lazy.firstVisibleItemScrollOffset,
            )
        }

        pendingTargetIndex = targetIndex
        onMove(fromInfo.index, targetIndex)
    }
}

@Composable
fun rememberDragDropState(
    onMove: (fromIndex: Int, toIndex: Int) -> Unit,
    lazyListState: LazyListState? = null,
    scrollState: ScrollState? = null,
    motion: DragDropMotion = rememberDragDropMotion(),
    scrollThreshold: Dp = 48.dp,
): DragDropState {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val thresholdPx = with(density) { scrollThreshold.toPx() }
    val maxScrollStepPx = with(density) { 20.dp.toPx() }
    val currentOnMove by rememberUpdatedState(onMove)

    return remember(lazyListState, scrollState, motion, thresholdPx, maxScrollStepPx) {
        DragDropState(
            lazyListState = lazyListState,
            scrollState = scrollState,
            motion = motion,
            scope = scope,
            scrollThresholdPx = thresholdPx,
            maxScrollStepPx = maxScrollStepPx,
            onMove = { from, to -> currentOnMove(from, to) },
        )
    }
}

fun Modifier.dragDropContainer(state: DragDropState): Modifier {
    return onGloballyPositioned { coordinates ->
        state.registerContainer(
            top = coordinates.positionInRoot().y,
            height = coordinates.size.height,
        )
    }
}

fun Modifier.dragDropItem(
    state: DragDropState,
    key: Any,
    index: Int,
    enabled: Boolean = true,
    onDragStarted: () -> Unit = {},
    onDragStopped: () -> Unit = {},
): Modifier = composed {
    DisposableEffect(state, key) {
        onDispose { state.unregisterItem(key) }
    }

    onGloballyPositioned { coordinates ->
        state.registerItem(
            key = key,
            index = index,
            top = coordinates.positionInRoot().y,
            height = coordinates.size.height,
        )
    }
        .zIndex(state.zIndexFor(key))
        .graphicsLayer {
            translationY = state.translationFor(key)
        }
        .dragDropGesture(
            stateKey = state,
            pointerKey = key,
            enabled = enabled,
            longPress = true,
            onDragStart = { state.startDrag(key) },
            onDrag = { state.drag(it) },
            onDragEnd = { state.stopDrag() },
            onDragStarted = onDragStarted,
            onDragStopped = onDragStopped,
        )
}

fun Modifier.dragDropHandle(
    state: DragDropState,
    key: Any,
    enabled: Boolean = true,
    onDragStarted: () -> Unit = {},
    onDragStopped: () -> Unit = {},
): Modifier = composed {
    dragDropGesture(
        stateKey = state,
        pointerKey = key,
        enabled = enabled,
        longPress = false,
        onDragStart = { state.startDrag(key) },
        onDrag = { state.drag(it) },
        onDragEnd = { state.stopDrag() },
        onDragStarted = onDragStarted,
        onDragStopped = onDragStopped,
    )
}

private fun Modifier.dragDropGesture(
    stateKey: Any?,
    pointerKey: Any?,
    enabled: Boolean,
    longPress: Boolean,
    onDragStart: () -> Unit,
    onDrag: (Float) -> Unit,
    onDragEnd: () -> Unit,
    onDragStarted: () -> Unit,
    onDragStopped: () -> Unit,
): Modifier = composed {
    val currentOnDragStart by rememberUpdatedState(onDragStart)
    val currentOnDrag by rememberUpdatedState(onDrag)
    val currentOnDragEnd by rememberUpdatedState(onDragEnd)
    val currentOnDragStarted by rememberUpdatedState(onDragStarted)
    val currentOnDragStopped by rememberUpdatedState(onDragStopped)

    pointerInput(stateKey, pointerKey, enabled, longPress) {
        if (!enabled) return@pointerInput

        val dragStart: (Offset) -> Unit = {
            currentOnDragStart()
            currentOnDragStarted()
        }
        val drag: (PointerInputChange, Offset) -> Unit = { change, amount ->
            change.consume()
            currentOnDrag(amount.y)
        }
        val dragEnd: () -> Unit = {
            currentOnDragEnd()
            currentOnDragStopped()
        }

        if (longPress) {
            detectDragGesturesAfterLongPress(
                onDragStart = dragStart,
                onDragEnd = dragEnd,
                onDragCancel = dragEnd,
                onDrag = drag,
            )
        } else {
            detectDragGestures(
                onDragStart = dragStart,
                onDragEnd = dragEnd,
                onDragCancel = dragEnd,
                onDrag = drag,
            )
        }
    }
}
