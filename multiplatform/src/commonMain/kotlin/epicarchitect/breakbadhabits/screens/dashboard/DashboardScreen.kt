package epicarchitect.breakbadhabits.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.infrastructure.Monetization
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.dashboard_empty_title
import epicarchitect.breakbadhabits.resources.dashboard_new_add
import epicarchitect.breakbadhabits.resources.dashboard_today_title
import epicarchitect.breakbadhabits.resources.settings
import epicarchitect.breakbadhabits.screens.root.LocalRootBackStack
import epicarchitect.breakbadhabits.screens.root.RootRoute
import epicarchitect.breakbadhabits.screens.root.push
import org.jetbrains.compose.resources.stringResource

private const val FREE_HABIT_LIMIT = 2

@Composable
fun DashboardScreen() {
    val state = rememberDashboardScreenState()
    val premium by Monetization.premiumCache.collectAsState()
    val purchasesAvailable = Monetization.purchasesAvailable
    val backStack = LocalRootBackStack.current
    val habits = state?.habits.orEmpty()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(key = "app_bar") {
                TopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = {
                        Text(
                            text = stringResource(Res.string.dashboard_today_title),
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    actions = {
                        IconButton(onClick = { backStack.push(RootRoute.Settings) }) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = stringResource(Res.string.settings)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            }

            if (habits.isEmpty()) {
                item(key = "empty_state") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillParentMaxHeight(0.7f)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            text = stringResource(Res.string.dashboard_empty_title)
                        )
                    }
                }
            } else {
                items(items = habits, key = { it.id }) { habit ->
                    HabitCard(
                        habit = habit,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }

        Button(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(16.dp),
            onClick = {
                val canAdd = purchasesAvailable.not() ||
                        premium.isPremium ||
                        habits.size < FREE_HABIT_LIMIT
                backStack.push(
                    if (canAdd) RootRoute.HabitEditing(habitId = null)
                    else RootRoute.Paywall(source = "habit_limit")
                )
            }
        ) {
            Text(text = stringResource(Res.string.dashboard_new_add))
        }
    }
}
