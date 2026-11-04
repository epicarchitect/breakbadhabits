package epicarchitect.breakbadhabits.uikit

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipe
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class AppScaffoldTest {

    @Test
    fun topBarHidesOnScrollDownAndReturnsOnScrollUp() = runComposeUiTest {
        setContent {
            PreviewTheme {
                val listState = rememberLazyListState()
                AppScaffold(
                    title = "Habits",
                    topBarScrollBehavior = rememberEnterAlwaysTopBarScrollBehavior(listState)
                ) { topBarHeight ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("list"),
                        contentPadding = PaddingValues(top = topBarHeight),
                        state = listState
                    ) {
                        items(40) { index ->
                            Text(
                                text = "Item $index",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(96.dp)
                            )
                        }
                    }
                }
            }
        }

        onNodeWithText("Habits").assertIsDisplayed()
        onNodeWithTag("list").performTouchInput { swipeUp() }
        waitForIdle()
        onNodeWithText("Habits").assertIsNotDisplayed()

        onNodeWithTag("list").performTouchInput { swipeDown() }
        waitForIdle()
        onNodeWithText("Habits").assertIsDisplayed()
    }

    @Test
    fun shortContentDoesNotHideTopBar() = runComposeUiTest {
        setContent {
            PreviewTheme {
                val listState = rememberLazyListState()
                AppScaffold(
                    title = "Habits",
                    topBarScrollBehavior = rememberEnterAlwaysTopBarScrollBehavior(listState)
                ) { topBarHeight ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("list"),
                        contentPadding = PaddingValues(top = topBarHeight),
                        state = listState
                    ) {
                        item {
                            Text(
                                text = "Only item",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(96.dp)
                            )
                        }
                    }
                }
            }
        }

        onNodeWithText("Habits").assertIsDisplayed()
        onNodeWithTag("list").performTouchInput { swipeUp() }
        waitForIdle()
        onNodeWithText("Habits").assertIsDisplayed()
    }

    @Test
    fun topBarAndContentMoveTogetherDuringSlowScroll() = runComposeUiTest {
        lateinit var behavior: AppTopBarScrollBehavior
        lateinit var listState: LazyListState

        setContent {
            PreviewTheme {
                val state = rememberLazyListState()
                listState = state
                behavior = rememberEnterAlwaysTopBarScrollBehavior(state)
                AppScaffold(
                    title = "Habits",
                    topBarScrollBehavior = behavior
                ) { topBarHeight ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("list"),
                        contentPadding = PaddingValues(top = topBarHeight),
                        state = state
                    ) {
                        items(40) { index ->
                            Text(
                                text = "Item $index",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(96.dp)
                            )
                        }
                    }
                }
            }
        }

        waitForIdle()
        val itemTopBefore = onNodeWithText("Item 0").getBoundsInRoot().top

        onNodeWithTag("list").performTouchInput {
            swipe(
                start = center,
                end = center + Offset(0f, -60f),
                durationMillis = 1000
            )
        }
        waitForIdle()

        val itemTopAfter = onNodeWithText("Item 0").getBoundsInRoot().top
        val scrollOffset = listState.scrollIndicatorState?.scrollOffset ?: 0
        val expectedHeightOffset = -minOf(scrollOffset, (-behavior.state.heightOffsetLimit).toInt()).toFloat()
        assertEquals(expectedHeightOffset, behavior.offsetPx, absoluteTolerance = 1f)
        assertTrue(itemTopAfter < itemTopBefore)
    }
    @Test
    fun topBarStartsReturningOnDownwardScroll() = runComposeUiTest {
        lateinit var behavior: AppTopBarScrollBehavior

        setContent {
            PreviewTheme {
                val listState = rememberLazyListState()
                behavior = rememberEnterAlwaysTopBarScrollBehavior(listState)
                AppScaffold(
                    title = "Habits",
                    topBarScrollBehavior = behavior
                ) { topBarHeight ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("list"),
                        contentPadding = PaddingValues(top = topBarHeight),
                        state = listState
                    ) {
                        items(40) { index ->
                            Text(
                                text = "Item $index",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(96.dp)
                            )
                        }
                    }
                }
            }
        }

        waitForIdle()
        onNodeWithTag("list").performTouchInput { swipeUp() }
        waitForIdle()
        val hiddenOffset = behavior.offsetPx
        assertTrue(hiddenOffset < 0f)

        onNodeWithTag("list").performTouchInput {
            swipe(
                start = center,
                end = center + Offset(0f, 60f),
                durationMillis = 1000
            )
        }
        waitForIdle()
        assertTrue(behavior.offsetPx > hiddenOffset)
    }

    @Test
    fun topBarFollowsScrollOffsetChangesOutsideActiveScroll() = runComposeUiTest {
        lateinit var behavior: AppTopBarScrollBehavior
        lateinit var listState: LazyListState

        setContent {
            PreviewTheme {
                val state = rememberLazyListState()
                listState = state
                behavior = rememberEnterAlwaysTopBarScrollBehavior(state)
                AppScaffold(
                    title = "Habits",
                    topBarScrollBehavior = behavior
                ) { topBarHeight ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("list"),
                        contentPadding = PaddingValues(top = topBarHeight),
                        state = state
                    ) {
                        items(40) { index ->
                            Text(
                                text = "Item $index",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(96.dp)
                            )
                        }
                    }
                }
            }
        }

        waitForIdle()
        assertEquals(0f, behavior.offsetPx, absoluteTolerance = 1f)

        runOnIdle { listState.dispatchRawDelta(200f) }
        waitForIdle()
        assertTrue(behavior.offsetPx < 0f)

        runOnIdle { listState.dispatchRawDelta(-200f) }
        waitForIdle()
        assertEquals(0f, behavior.offsetPx, absoluteTolerance = 1f)
    }

    @Test
    fun variableHeightListTopBarTracksActualScroll() = runComposeUiTest {
        lateinit var behavior: AppTopBarScrollBehavior
        lateinit var listState: LazyListState
        var firstItemHeightPx = 0f

        setContent {
            PreviewTheme {
                val density = LocalDensity.current
                firstItemHeightPx = with(density) { 8.dp.toPx() }
                val state = rememberLazyListState()
                listState = state
                behavior = rememberEnterAlwaysTopBarScrollBehavior(state)
                AppScaffold(
                    title = "Habits",
                    topBarScrollBehavior = behavior
                ) { topBarHeight ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("list"),
                        contentPadding = PaddingValues(top = topBarHeight),
                        state = state
                    ) {
                        items(20) { index ->
                            Text(
                                text = "Item $index",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(
                                        when (index) {
                                            0 -> 8.dp
                                            1 -> 400.dp
                                            else -> 96.dp
                                        }
                                    )
                            )
                        }
                    }
                }
            }
        }

        waitForIdle()
        onNodeWithTag("list").performTouchInput {
            swipe(
                start = center,
                end = center + Offset(0f, -50f),
                durationMillis = 2000
            )
        }
        waitForIdle()

        assertEquals(1, listState.firstVisibleItemIndex)
        val actualScrollOffset = firstItemHeightPx + listState.firstVisibleItemScrollOffset
        val expectedOffset = -minOf(
            actualScrollOffset.toInt(),
            (-behavior.state.heightOffsetLimit).toInt()
        ).toFloat()
        assertEquals(expectedOffset, behavior.offsetPx, absoluteTolerance = 2f)
    }

}
