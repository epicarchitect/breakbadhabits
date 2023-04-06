@file:OptIn(ExperimentalAnimationApi::class)

package breakbadhabits.android.app.ui.dashboard

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import breakbadhabits.android.app.R
import breakbadhabits.android.app.di.LocalUiModule
import breakbadhabits.android.app.icons.resourceId
import breakbadhabits.app.presentation.dashboard.DashboardHabitItem
import breakbadhabits.foundation.controller.LoadingController
import breakbadhabits.foundation.datetime.duration
import breakbadhabits.foundation.uikit.Card
import breakbadhabits.foundation.uikit.IconButton
import breakbadhabits.foundation.uikit.LoadingBox
import breakbadhabits.foundation.uikit.LocalResourceIcon
import breakbadhabits.foundation.uikit.button.Button
import breakbadhabits.foundation.uikit.ext.collectState
import breakbadhabits.foundation.uikit.text.Text

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DashboardScreen(
    habitItemsController: LoadingController<List<DashboardHabitItem>>,
    onHabitClick: (Int) -> Unit,
    onAddTrackClick: (Int) -> Unit,
    onHabitCreationClick: () -> Unit,
    onAppSettingsClick: () -> Unit
) {
    val itemsState by habitItemsController.collectState()

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
                    text = stringResource(R.string.app_name),
                    type = Text.Type.Title,
                    priority = Text.Priority.High
                )

                IconButton(
                    onClick = onAppSettingsClick
                ) {
                    LocalResourceIcon(R.drawable.ic_settings)
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
                text = stringResource(R.string.habits_newHabit),
                type = Button.Type.Main,
                icon = {
                    LocalResourceIcon(resourceId = R.drawable.ic_add)
                }
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
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center,
            text = stringResource(R.string.habits_empty)
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
    val uiModule = LocalUiModule.current
    val durationFormatter = uiModule.durationFormatter

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
                    LocalResourceIcon(item.habit.icon.resourceId)
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
                    LocalResourceIcon(R.drawable.ic_time)

                    Text(
                        modifier = Modifier.padding(start = 12.dp),
                        text = item.abstinence?.let {
                            durationFormatter.format(it.instantRange.duration)
                        } ?: stringResource(R.string.habits_noEvents),
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
                LocalResourceIcon(R.drawable.ic_reset)
            }
        }
    }
}