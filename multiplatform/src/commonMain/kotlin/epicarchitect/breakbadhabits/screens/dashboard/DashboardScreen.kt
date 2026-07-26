package epicarchitect.breakbadhabits.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.environment.LocalAppEnvironment
import epicarchitect.breakbadhabits.screens.root.LocalRootNavController
import epicarchitect.breakbadhabits.screens.root.RootRoute
import epicarchitect.breakbadhabits.uikit.SimpleTopAppBar

@Composable
fun DashboardScreen() {
    val state = rememberDashboardScreenState() ?: return

    val environment = LocalAppEnvironment.current
    val navController = LocalRootNavController.current
    val strings = environment.resources.strings.appDashboardStrings

    Column {
        SimpleTopAppBar(
            modifier = Modifier.fillMaxWidth(),
            title = strings.titleText()
        )

        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            if (state.habits.isEmpty()) {
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center),
                    textAlign = TextAlign.Center,
                    text = strings.emptyHabitsText()
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = 100.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(
                        items = state.habits,
                        key = { it.id }
                    ) {
                        HabitCard(it)
                    }
                }
            }

            Button(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                onClick = {
                    navController.navigate(
                        RootRoute.HabitEditing(
                            habitId = null
                        )
                    )
                }
            ) {
                Text(
                    text = strings.newHabitButtonText(),
                )
            }
        }
    }
}