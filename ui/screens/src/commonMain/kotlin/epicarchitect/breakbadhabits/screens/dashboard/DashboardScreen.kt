package epicarchitect.breakbadhabits.screens.dashboard

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.foundation.controller.LoadingController
import epicarchitect.breakbadhabits.foundation.datetime.duration
import epicarchitect.breakbadhabits.foundation.uikit.Card
import epicarchitect.breakbadhabits.foundation.uikit.IconButton
import epicarchitect.breakbadhabits.foundation.uikit.LoadingBox
import epicarchitect.breakbadhabits.foundation.uikit.button.Button
import epicarchitect.breakbadhabits.foundation.uikit.text.Text
import epicarchitect.breakbadhabits.presentation.dashboard.DashboardHabitItem
import epicarchitect.breakbadhabits.screens.LocalAppModule

val LocalDashboardResources = compositionLocalOf<DashboardResources> {
    error("LocalDashboardResources not provided")
}

interface DashboardResources {
    val titleText: String
    val newHabitButtonText: String
    val emptyHabitsText: String
    val habitHasNoEvents: String
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Dashboard(
    habitItemsController: LoadingController<List<DashboardHabitItem>>,
    onHabitClick: (Int) -> Unit,
    onAddTrackClick: (Int) -> Unit,
    onHabitCreationClick: () -> Unit,
    onAppSettingsClick: () -> Unit
) {
    val resources = LocalDashboardResources.current
    val itemsState by habitItemsController.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = resources.titleText,
                    type = Text.Type.Title,
                    priority = Text.Priority.High
                )

                IconButton(
                    onClick = onAppSettingsClick
                ) {
//                    LocalResourceIcon(R.drawable.ic_settings)
                }
            }

            LoadingBox(habitItemsController) { items ->
                if (items.isEmpty()) {
                    NotExistsHabits()
                } else {
                    LoadedHabits(
                        items = items,
                        onResetClick = onAddTrackClick,
                        onItemClick = onHabitClick
                    )
                }
            }
        }

        AnimatedVisibility(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd),
            visible = itemsState !is LoadingController.State.Loading,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            Button(
                onClick = onHabitCreationClick,
                text = resources.newHabitButtonText,
                type = Button.Type.Main,
//                icon = {
//                    LocalResourceIcon(resourceId = R.drawable.ic_add)
//                }
            )
        }
    }
}

@Composable
private fun LoadedHabits(
    items: List<DashboardHabitItem>,
    onResetClick: (Int) -> Unit,
    onItemClick: (Int) -> Unit
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
            items = items,
            key = { it.habit.id }
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
    val resources = LocalDashboardResources.current
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center,
            text = resources.emptyHabitsText
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LazyItemScope.HabitItem(
    item: DashboardHabitItem,
    onClick: () -> Unit,
    onResetClick: () -> Unit
) {
    val resources = LocalDashboardResources.current
    val uiModule = LocalAppModule.current.ui
    val durationFormatter = uiModule.format.durationFormatter

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
//                    LocalResourceIcon(item.habit.icon.resourceId)
                    Text(
                        modifier = Modifier.padding(start = 12.dp),
                        text = item.habit.name,
                        type = Text.Type.Title,
                        priority = Text.Priority.Medium
                    )
                }
                Row(
                    modifier = Modifier.padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
//                    LocalResourceIcon(R.drawable.ic_time)

                    Text(
                        modifier = Modifier.padding(start = 12.dp),
                        text = item.abstinence?.let {
                            durationFormatter.format(it.dateTimeRange.duration)
                        } ?: resources.habitHasNoEvents,
                        type = Text.Type.Description,
                        priority = Text.Priority.Medium
                    )
                }
            }

            IconButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp),
                onClick = onResetClick
            ) {
//                LocalResourceIcon(R.drawable.ic_reset)
            }
        }
    }
}