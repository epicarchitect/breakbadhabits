package epicarchitect.breakbadhabits.uikit

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import kotlinx.coroutines.flow.collect

@Stable
interface AppTopBarScrollBehavior : TopAppBarScrollBehavior {
    val offsetPx: Float
    fun onTopBarHeightChanged(heightPx: Float)
}

@Composable
fun rememberEnterAlwaysTopBarScrollBehavior(
    scrollableState: ScrollableState
): AppTopBarScrollBehavior {
    val state = rememberTopAppBarState()
    val behavior = remember(scrollableState, state) {
        EnterAlwaysAppTopBarScrollBehavior(state = state)
    }

    LaunchedEffect(behavior, scrollableState) {
        var previousOffset = scrollableState.scrollIndicatorState?.scrollOffset
            ?: Int.MAX_VALUE

        snapshotFlow {
            scrollableState.scrollIndicatorState?.scrollOffset ?: Int.MAX_VALUE
        }.collect { offset ->
            if (!scrollableState.isScrollInProgress) {
                behavior.onScrollOffsetChanged(previousOffset, offset)
            }
            previousOffset = offset
        }
    }

    return behavior
}

private class EnterAlwaysAppTopBarScrollBehavior(
    override val state: TopAppBarState
) : AppTopBarScrollBehavior {
    override val isPinned: Boolean = false
    override val snapAnimationSpec: AnimationSpec<Float>? = null
    override val flingAnimationSpec: DecayAnimationSpec<Float>? = null

    override val offsetPx: Float
        get() = state.heightOffset

    override val nestedScrollConnection: NestedScrollConnection =
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: androidx.compose.ui.geometry.Offset,
                available: androidx.compose.ui.geometry.Offset,
                source: androidx.compose.ui.input.nestedscroll.NestedScrollSource
            ): androidx.compose.ui.geometry.Offset {
                state.heightOffset += consumed.y
                return androidx.compose.ui.geometry.Offset.Zero
            }
        }

    override fun onTopBarHeightChanged(heightPx: Float) {
        state.heightOffsetLimit = -heightPx
        state.heightOffset = state.heightOffset.coerceIn(-heightPx, 0f)
    }

    fun onScrollOffsetChanged(previousOffset: Int, offset: Int) {
        if (previousOffset == Int.MAX_VALUE || offset == Int.MAX_VALUE) return
        state.heightOffset += (previousOffset - offset).toFloat()
    }
}
