package epicarchitect.epicstore.navigation.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import epicarchitect.epicstore.compose.EpicStore

@Composable
fun EpicHavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String,
    route: String? = null,
    builder: NavGraphBuilder.() -> Unit
) {
    CompositionLocalProvider(
        LocalNavController provides navController
    ) {
        NavHost(
            navController = LocalNavController.current,
            startDestination = startDestination,
            modifier = modifier,
            route = route,
            builder = builder
        )
    }
}

fun NavGraphBuilder.epicStoreComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    onEntryCleared: ((key: Any?, value: Any?) -> Unit)? = null,
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(
        route,
        arguments,
        deepLinks
    ) {
        val navController = LocalNavController.current
        EpicStore(
            clearWhen = {
                navController.backQueue.find {
                    it.destination.route == route
                } == null
            },
            onEntryCleared = onEntryCleared,
            content = {
                content(it)
            }
        )
    }
}

