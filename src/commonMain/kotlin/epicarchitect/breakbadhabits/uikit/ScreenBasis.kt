package epicarchitect.breakbadhabits.uikit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.data.AppData
import epicarchitect.breakbadhabits.data.resources.icons.Icon
import epicarchitect.breakbadhabits.uikit.button.Button
import epicarchitect.breakbadhabits.uikit.text.Text

object ScreenBasis {

    sealed interface ActionButton {
        @Composable
        fun Content()
    }

    data class FloatingActionButton(
        private val icon: Icon?,
        private val title: String,
        private val onClick: () -> Unit
    ) : ActionButton {
        @Composable
        override fun Content() {
            Button(
                onClick = onClick,
                text = title,
                type = Button.Type.Main,
                icon = icon?.let {
                    {
                        Icon(it)
                    }
                }
            )
        }
    }

    data class IconActionButton(
        private val icon: Icon,
        private val onClick: () -> Unit
    ) : ActionButton {
        @Composable
        override fun Content() {
            IconButton(
                icon = icon,
                onClick = onClick
            )
        }
    }

    data class BackActionButton(
        private val onClick: () -> Unit
    ) : ActionButton {
        @Composable
        override fun Content() {
            IconButton(
                icon = AppData.resources.icons.commonIcons.arrowBack,
                onClick = onClick
            )
        }
    }

    sealed interface TopBar {
        @Composable
        fun Content()
    }

    data class TitleTopBar(
        private val title: String,
        private val rightActionButton: ActionButton? = null,
        private val leftActionButton: ActionButton? = null,
    ) : TopBar {

        @Composable
        override fun Content() {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                content = {
                    leftActionButton?.Content()
                    Text(
                        text = title,
                        type = Text.Type.Title,
                        priority = Text.Priority.High
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    rightActionButton?.Content()
                }
            )
        }
    }

    data class CustomTopBar(
        private val content: @Composable RowScope.() -> Unit,
        private val rightActionButton: ActionButton? = null,
        private val leftActionButton: ActionButton? = null,
    ) : TopBar {

        @Composable
        override fun Content() {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                content = {
                    leftActionButton?.Content()
                    content()
                    Spacer(modifier = Modifier.weight(1f))
                    rightActionButton?.Content()
                }
            )
        }
    }
}

@Composable
fun SimpleTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    actions: @Composable () -> Unit = {}
) {
    Row(
        modifier = modifier.height(40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onBackClick != null) {
            IconButton(
                icon = AppData.resources.icons.commonIcons.navigationBack,
                onClick = onBackClick
            )
        } else {
            Spacer(modifier = Modifier.padding(start = 24.dp))
        }

        Text(
            text = title,
            type = Text.Type.Title
        )

        Spacer(modifier = Modifier.weight(1f))

        actions()
    }
}

@Composable
fun ScreenBasis(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    topBar: ScreenBasis.TopBar? = null,
    floatingActionButton: ScreenBasis.ActionButton? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            floatingActionButton?.Content()
        },
        topBar = {
            topBar?.Content()
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .then(contentModifier),
                content = content
            )
        }
    )
}

data class FloatingActionButtonConfig(
    val text: String,
    val onClick: () -> Unit
)

@Composable
fun ScreenBasis(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    topBar: ScreenBasis.TopBar? = null,
    floatingActionButtonConfig: FloatingActionButtonConfig? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            if (floatingActionButtonConfig != null) {
                Button(
                    onClick = floatingActionButtonConfig.onClick,
                    text = floatingActionButtonConfig.text
                )
            }
        },
        topBar = {
            topBar?.Content()
        },
        content = content
    )
}