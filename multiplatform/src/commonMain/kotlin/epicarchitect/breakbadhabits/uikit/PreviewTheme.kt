package epicarchitect.breakbadhabits.uikit

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import epicarchitect.breakbadhabits.analytics.AnalyticsOperator
import epicarchitect.breakbadhabits.infrastructure.AnalyticsOperator as AnalyticsOperatorHolder
import epicarchitect.breakbadhabits.screens.root.LocalNavBackStack

@Composable
fun PreviewTheme(
    content: @Composable () -> Unit
) {
    remember {
        AnalyticsOperatorHolder = PreviewAnalyticsOperator
    }
    val navBackStack = remember { NavBackStack<NavKey>() }

    CompositionLocalProvider(LocalNavBackStack provides navBackStack) {
        AppTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
                content = content
            )
        }
    }
}

private object PreviewAnalyticsOperator : AnalyticsOperator {
    override var isEnabled = false

    override fun logEvent(name: String, parameters: Map<String, Any>) = Unit

    override fun setUserProperty(name: String, value: String?) = Unit

    override fun logError(throwable: Throwable) = Unit
}
