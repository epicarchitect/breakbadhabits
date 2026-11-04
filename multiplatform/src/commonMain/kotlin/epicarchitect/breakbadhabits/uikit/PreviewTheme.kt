package epicarchitect.breakbadhabits.uikit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import epicarchitect.breakbadhabits.analytics.AnalyticsOperator
import epicarchitect.breakbadhabits.screens.root.LocalNavBackStack
import epicarchitect.breakbadhabits.infrastructure.AnalyticsOperator as AnalyticsOperatorHolder

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
            content()
        }
    }
}

private object PreviewAnalyticsOperator : AnalyticsOperator {
    override var isEnabled = false

    override fun logEvent(name: String, parameters: Map<String, Any>) = Unit

    override fun setUserProperty(name: String, value: String?) = Unit

    override fun logError(throwable: Throwable) = Unit
}
