package epicarchitect.breakbadhabits.uikit

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.back
import org.jetbrains.compose.resources.stringResource
import kotlin.math.roundToInt

val LocalAppTopBarScrollConnection =
    staticCompositionLocalOf<NestedScrollConnection?> { null }

@Composable
fun AppScaffold(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    contentWidth: AdaptiveContentWidth = AdaptiveContentWidth.Reading,
    topBarScrollBehavior: AppTopBarScrollBehavior? = null,
    content: @Composable BoxScope.(Dp) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                title = title,
                navigationIcon = navigationIcon,
                actions = actions,
                scrollBehavior = topBarScrollBehavior
            )
        },
        snackbarHost = snackbarHost
    ) { innerPadding ->
        val topBarHeight = innerPadding.calculateTopPadding()

        val bodyModifier = Modifier
            .fillMaxSize()
            .imePadding()
            .let { modifier ->
                if (topBarScrollBehavior != null) {
                    modifier.nestedScroll(topBarScrollBehavior.nestedScrollConnection)
                } else {
                    modifier
                }
            }

        CompositionLocalProvider(
            LocalAppTopBarScrollConnection provides topBarScrollBehavior?.nestedScrollConnection
        ) {
            AdaptiveContent(
                modifier = bodyModifier,
                width = contentWidth
            ) {
                content(topBarHeight)
            }
        }
    }
}

private fun Modifier.appTopBarScroll(
    behavior: AppTopBarScrollBehavior?
): Modifier = onSizeChanged { size ->
    behavior?.onTopBarHeightChanged(size.height.toFloat())
}.offset {
    IntOffset(
        x = 0,
        y = (behavior?.offsetPx ?: 0f).roundToInt()
    )
}

@Composable
private fun AppTopBar(
    title: String,
    navigationIcon: (@Composable () -> Unit)?,
    actions: @Composable RowScope.() -> Unit,
    scrollBehavior: AppTopBarScrollBehavior?
) {
    CenterAlignedTopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .appTopBarScroll(scrollBehavior),
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            navigationIcon?.invoke()
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
fun AppBackButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(Res.string.back)
        )
    }
}

@Preview(widthDp = 360, heightDp = 320, name = "AppScaffold compact")
@Preview(widthDp = 900, heightDp = 320, name = "AppScaffold wide")
@Composable
private fun AppScaffoldPreview() {
    PreviewTheme {
        AppScaffold(
            title = "Habits",
            navigationIcon = { AppBackButton(onClick = {}) }
        ) { _ ->
            Text(
                text = "Content",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
