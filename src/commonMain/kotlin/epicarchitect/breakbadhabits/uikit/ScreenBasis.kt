package epicarchitect.breakbadhabits.uikit

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import epicarchitect.breakbadhabits.Environment
import epicarchitect.breakbadhabits.uikit.text.Text
import epicarchitect.breakbadhabits.uikit.theme.AppTheme

@Composable
fun SimpleTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    shadowElevation: Dp = 0.dp,
    onBackClick: (() -> Unit)? = null,
    actions: @Composable () -> Unit = {}
) {
    Surface(
        modifier = Modifier.zIndex(2f).then(modifier),
        shadowElevation = shadowElevation,
        color = AppTheme.colorScheme.background
    ) {
        Row(
            modifier = Modifier.height(46.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onBackClick != null) {
                IconButton(
                    icon = Environment.resources.icons.commonIcons.navigationBack,
                    onClick = onBackClick
                )
            } else {
                Spacer(modifier = Modifier.padding(start = 24.dp))
            }

            AnimatedContent(
                targetState = title,
                transitionSpec = {
                    fadeIn(tween(220)).togetherWith(fadeOut(tween(220)))
                }
            ) {
                Text(
                    text = it,
                    type = Text.Type.Title
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            actions()
        }
    }
}

@Composable
fun ScrollState.animatedShadowElevation(
    triggerScrollValue: Dp = 16.dp,
    targetValue: Dp = 2.dp
): State<Dp> {
    val density = LocalDensity.current

    val showShadow by remember {
        derivedStateOf {
            with(density) {
                value.toDp() >= triggerScrollValue
            }
        }
    }
    return animateDpAsState(
        targetValue = if (showShadow) targetValue else 0.dp
    )
}

@Composable
fun LazyListState.animatedShadowElevation(
    triggerScrollValue: Dp = 16.dp,
    shadowElevation: Dp = 2.dp
): State<Dp> {
    val density = LocalDensity.current

    val showShadow by remember {
        derivedStateOf {
            with(density) {
                firstVisibleItemIndex > 0 ||
                        -(layoutInfo.visibleItemsInfo.firstOrNull()?.offset?.toDp()
                            ?: 0.dp) > triggerScrollValue
            }
        }
    }
    return animateDpAsState(
        targetValue = if (showShadow) shadowElevation else 0.dp
    )
}

@Composable
fun SimpleScrollableScreen(
    title: String,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    onBackClick: (() -> Unit)? = null,
    actions: @Composable () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier) {
        val shadowElevation by scrollState.animatedShadowElevation()

        SimpleTopAppBar(
            title = title,
            shadowElevation = shadowElevation,
            onBackClick = onBackClick,
            actions = actions
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            content = content
        )
    }
}