@file:OptIn(ExperimentalAnimationApi::class)

package breakbadhabits.android.app.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.with
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import breakbadhabits.android.app.LocalHabitIconResources
import breakbadhabits.android.app.LocalPresentationModule
import breakbadhabits.android.app.R
import breakbadhabits.entity.Habit
import breakbadhabits.presentation.HabitsDashboardViewModel
import breakbadhabits.ui.kit.Button
import breakbadhabits.ui.kit.Card
import breakbadhabits.ui.kit.Icon
import breakbadhabits.ui.kit.IconButton
import breakbadhabits.ui.kit.InteractionType
import breakbadhabits.ui.kit.ProgressIndicator
import breakbadhabits.ui.kit.Text
import breakbadhabits.ui.kit.Title

@Composable
fun HabitsScreen(
    openHabit: (Habit.Id) -> Unit,
    openHabitEventCreation: (Habit.Id) -> Unit,
    openHabitCreation: () -> Unit,
    openSettings: () -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val dashboardViewModel = viewModel {
        presentationModule.createHabitsDashboardViewModel()
    }
    val dashboardState by dashboardViewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedContent(
            modifier = Modifier.fillMaxSize(),
            targetState = dashboardState,
            transitionSpec = { fadeIn() with fadeOut() }
        ) { state ->
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    Title(
                        modifier = Modifier.align(Alignment.Center),
                        text = stringResource(R.string.app_name)
                    )

                    IconButton(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        onClick = openSettings
                    ) {
                        Icon(painterResource(R.drawable.ic_settings))
                    }
                }

                when (state) {
                    is HabitsDashboardViewModel.State.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            ProgressIndicator()
                        }
                    }

                    is HabitsDashboardViewModel.State.NotExist -> {
                        NotExistsHabits()
                    }

                    is HabitsDashboardViewModel.State.Loaded -> {
                        LoadedHabits(
                            state = state,
                            onResetClick = openHabitEventCreation,
                            onItemClick = openHabit
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            modifier = Modifier
                .padding(24.dp)
                .align(Alignment.BottomCenter),
            visible = dashboardState !is HabitsDashboardViewModel.State.Loading,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            Button(
                onClick = openHabitCreation,
                text = stringResource(R.string.habits_newHabit),
                interactionType = InteractionType.MAIN
            )
        }
    }
}

@Composable
private fun LoadedHabits(
    state: HabitsDashboardViewModel.State.Loaded,
    onResetClick: (Habit.Id) -> Unit,
    onItemClick: (Habit.Id) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 4.dp,
            bottom = 100.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            items = state.items,
            key = { it.habit.id.value }
        ) { item ->
            HabitItem(
                item = item,
                onClick = { onItemClick(item.habit.id) },
                onResetClick = { onResetClick(item.habit.id) }
            )
        }
    }
}

@Composable
private fun NotExistsHabits() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp),
            textAlign = TextAlign.Center,
            text = stringResource(R.string.habits_empty)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LazyItemScope.HabitItem(
    item: HabitsDashboardViewModel.HabitItem,
    onClick: () -> Unit,
    onResetClick: () -> Unit
) {
    val habitIconResources = LocalHabitIconResources.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateItemPlacement()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
        ) {
            Column(
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 16.dp,
                    bottom = 16.dp,
                    end = 50.dp
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(habitIconResources[item.habit.iconResource.iconId]),
                    )
                    Title(
                        modifier = Modifier.padding(start = 12.dp),
                        text = item.habit.name.value
                    )
                }
                Row(
                    modifier = Modifier.padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_time)
                    )

                    val abstinence by item.abstinenceTime.collectAsState()
                    Text(
                        modifier = Modifier.padding(start = 12.dp),
                        text = abstinence ?: "nothing"
                    )
                }
            }

            IconButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp),
                onClick = onResetClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_reset)
                )
            }
        }
    }
}