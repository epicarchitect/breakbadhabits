package epicarchitect.breakbadhabits.screens.habits.details

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.back
import epicarchitect.breakbadhabits.resources.settings
import epicarchitect.breakbadhabits.habits.habitProgress
import epicarchitect.breakbadhabits.screens.root.LocalRootBackStack
import epicarchitect.breakbadhabits.screens.root.RootRoute
import epicarchitect.breakbadhabits.screens.root.push
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.Uuid
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Alignment

@Composable
fun HabitDetailsScreen(habitId: Uuid) {
    val state = rememberHabitDetailsScreenState(habitId) ?: return
    val backStack = LocalRootBackStack.current

    val bottomInset = WindowInsets.navigationBars
        .asPaddingValues()
        .calculateBottomPadding()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        contentPadding = PaddingValues(bottom = bottomInset),
        state = rememberLazyListState()
    ) {
        item(key = "app_bar") {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = state.habit.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { backStack.removeLastOrNull() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.back)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { backStack.push(RootRoute.HabitEditing(habitId)) }) {
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

        item(key = "content") {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(max = 600.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(Modifier.height(16.dp))
                    HabitDetailsHeaderSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        state = state
                    )

                    state.abstinence?.let { duration ->
                        Spacer(Modifier.height(16.dp))
                        HabitStageCard(
                            progress = habitProgress(duration),
                            onAllStagesClick = {
                                backStack.push(RootRoute.HabitStages(habitId))
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    if (state.habitEventRecords.isNotEmpty()) {
                        HabitDetailsCalendarCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            state = state
                        )
                        Spacer(Modifier.height(16.dp))
                    }

                    if (state.abstinenceHistogramValues.size > 2) {
                        HabitDetailsHistogramCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            state = state
                        )
                        Spacer(Modifier.height(16.dp))
                    }

                    if (state.statistics.isNotEmpty()) {
                        HabitDetailsStatisticsCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            state = state
                        )
                        Spacer(Modifier.height(16.dp))
                    }

                    Spacer(Modifier.height(24.dp))
    
                }
            }
        }
    }
}
