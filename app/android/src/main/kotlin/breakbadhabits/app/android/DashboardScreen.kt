@file:OptIn(ExperimentalAnimationApi::class)

package breakbadhabits.app.android

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.presentation.dashboard.DashboardViewModel
import breakbadhabits.framework.uikit.button.Button
import breakbadhabits.framework.uikit.Card
import breakbadhabits.framework.uikit.Icon
import breakbadhabits.framework.uikit.IconButton
import breakbadhabits.framework.uikit.button.InteractionType
import breakbadhabits.framework.uikit.ProgressIndicator
import breakbadhabits.framework.uikit.text.Text
import breakbadhabits.framework.uikit.text.Title

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DashboardScreen(
    openHabit: (Habit.Id) -> Unit,
    openHabitEventCreation: (Habit.Id) -> Unit,
    openHabitCreation: () -> Unit,
    openSettings: () -> Unit
) {
    val presentationModule = LocalPresentationModule.current
    val dashboardViewModel = viewModel {
        presentationModule.createDashboardViewModel()
    }
    val dashboardItems by dashboardViewModel.items.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Title(
                    text = stringResource(R.string.app_name)
                )

                IconButton(
                    onClick = openSettings
                ) {
                    Icon(painterResource(R.drawable.ic_settings))
                }
            }


            when (val state = dashboardItems) {
                is DashboardViewModel.ItemsState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ProgressIndicator()
                    }
                }

                is DashboardViewModel.ItemsState.NotExist -> {
                    NotExistsHabits()
                }

                is DashboardViewModel.ItemsState.Loaded -> {
                    LoadedHabits(
                        itemsState = state,
                        onResetClick = openHabitEventCreation,
                        onItemClick = openHabit
                    )
                }
            }
        }

        AnimatedVisibility(
            modifier = Modifier
                .padding(24.dp)
                .align(Alignment.BottomCenter),
            visible = dashboardItems !is DashboardViewModel.ItemsState.Loading,
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
    itemsState: DashboardViewModel.ItemsState.Loaded,
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
            items = itemsState.items,
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
    item: DashboardViewModel.HabitItem,
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

                    Text(
                        modifier = Modifier.padding(start = 12.dp),
                        text = item.abstinenceTime ?: stringResource(R.string.habits_noEvents)
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