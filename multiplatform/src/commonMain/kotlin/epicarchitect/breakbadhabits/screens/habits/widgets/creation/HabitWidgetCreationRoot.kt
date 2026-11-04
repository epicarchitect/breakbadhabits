package epicarchitect.breakbadhabits.screens.habits.widgets.creation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import epicarchitect.breakbadhabits.screens.monetization.PaywallScreen
import epicarchitect.breakbadhabits.screens.root.LocalNavBackStack
import epicarchitect.breakbadhabits.uikit.AppTheme
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus
import kotlinx.serialization.modules.polymorphic

@Serializable
private sealed interface WidgetCreationRoute : NavKey {
    @Serializable
    data class Config(val systemWidgetId: Int) : WidgetCreationRoute

    @Serializable
    data class Paywall(val source: String) : WidgetCreationRoute
}

@OptIn(ExperimentalSerializationApi::class)
private val widgetCreationSavedStateConfiguration = SavedStateConfiguration {
    serializersModule += SerializersModule {
        polymorphic(NavKey::class) {
            subclassesOfSealed<WidgetCreationRoute>()
        }
    }
}

@Composable
fun HabitWidgetCreationRoot(
    systemWidgetId: Int,
    onDone: () -> Unit,
    onCancel: () -> Unit
) {
    val navBackStack = rememberNavBackStack(
        widgetCreationSavedStateConfiguration,
        WidgetCreationRoute.Config(systemWidgetId)
    )

    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
                    .imePadding()
            ) {
                CompositionLocalProvider(
                    LocalNavBackStack provides navBackStack
                ) {
                    NavDisplay(
                        modifier = Modifier.fillMaxSize(),
                        backStack = navBackStack,
                        onBack = {
                            if (navBackStack.size > 1) {
                                navBackStack.removeLastOrNull()
                            } else {
                                onCancel()
                            }
                        },
                        entryProvider = entryProvider {
                            entry<WidgetCreationRoute.Config> { route ->
                                HabitWidgetCreationScreen(
                                    systemWidgetId = route.systemWidgetId,
                                    onDone = onDone,
                                    onCancel = onCancel,
                                    onOpenPremium = {
                                        if (navBackStack.lastOrNull() !is WidgetCreationRoute.Paywall) {
                                            navBackStack.add(
                                                WidgetCreationRoute.Paywall("widget")
                                            )
                                        }
                                    }
                                )
                            }
                            entry<WidgetCreationRoute.Paywall> { route ->
                                PaywallScreen(source = route.source)
                            }
                        }
                    )
                }
            }
        }
    }
}
