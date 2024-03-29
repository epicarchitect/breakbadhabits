package epicarchitect.breakbadhabits.screens.dashboard

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.foundation.uikit.Card
import epicarchitect.breakbadhabits.foundation.uikit.Icon
import epicarchitect.breakbadhabits.foundation.uikit.IconButton
import epicarchitect.breakbadhabits.foundation.uikit.LoadingBox
import epicarchitect.breakbadhabits.foundation.uikit.button.Button
import epicarchitect.breakbadhabits.foundation.uikit.text.Text
import epicarchitect.breakbadhabits.presentation.dashboard.DashboardViewModel
import epicarchitect.breakbadhabits.screens.LocalAppModule
import epicarchitect.breakbadhabits.ui.icons.Icons

@Composable
fun Dashboard(
    viewModel: DashboardViewModel,
    onHabitCreationClick: () -> Unit,
    onAppSettingsClick: () -> Unit
) {
    val resources = LocalDashboardResources.current

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
                    onClick = onAppSettingsClick,
                    icon = Icons.Settings
                )
            }

            LoadingBox(
                modifier = Modifier.fillMaxSize(),
                controller = viewModel.itemsLoadingController
            ) { items ->
                if (items.isEmpty()) {
                    NotExistsHabits()
                } else {
                    LoadedHabits(items)
                }

                Button(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomEnd),
                    onClick = onHabitCreationClick,
                    text = resources.newHabitButtonText,
                    type = Button.Type.Main,
                    icon = {
                        Icon(Icons.Add)
                    }
                )
            }
        }
    }
}

@Composable
private fun LoadedHabits(
    items: List<DashboardViewModel.HabitItemViewModel>
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
            HabitItem(item)
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
    item: DashboardViewModel.HabitItemViewModel
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
                .clickable(onClick = item.openDetailsController::request)
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
                    Icon(item.habit.icon)

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
                    Icon(Icons.Time)

                    Text(
                        modifier = Modifier.padding(start = 12.dp),
                        text = item.abstinence?.let {
                            durationFormatter.format(it.duration)
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
                onClick = item.resetController::request,
                icon = Icons.Replay
            )
        }
    }
}