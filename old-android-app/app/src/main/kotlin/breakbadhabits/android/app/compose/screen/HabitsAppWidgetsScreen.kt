package breakbadhabits.android.app.compose.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import breakbadhabits.android.app.R
import breakbadhabits.android.app.appDependencies
import breakbadhabits.android.compose.ui.Card
import breakbadhabits.android.compose.ui.Text
import breakbadhabits.android.compose.ui.Title
import epicarchitect.epicstore.compose.epicStoreItems
import epicarchitect.epicstore.compose.rememberEpicStoreEntry

@Composable
fun HabitsAppWidgetsScreen(
    openHabitAppWidgetConfigEditing: (configId: Int) -> Unit
) {
    val habitsAppWidgetConfigIdsFeature = rememberEpicStoreEntry {
        appDependencies.createHabitsAppWidgetConfigIdsFeature()
    }

    val configIds by habitsAppWidgetConfigIdsFeature.state.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (configIds.isEmpty()) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp),
                text = stringResource(R.string.habitsAppWidgets_empty),
                textAlign = TextAlign.Center
            )
        } else {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Title(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp),
                    text = stringResource(R.string.main_widgets)
                )

                LazyColumn(
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = 160.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    epicStoreItems(configIds) { configid ->
                        WidgetConfigItem(
                            configId = configid,
                            onClick = {
                                openHabitAppWidgetConfigEditing(configid)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WidgetConfigItem(
    configId: Int,
    onClick: () -> Unit
) {
    val habitsAppWidgetTitleFeature = rememberEpicStoreEntry {
        appDependencies.createHabitsAppWidgetTitleFeature(configId)
    }

    val habitsAppWidgetHabitIdsFeature = rememberEpicStoreEntry {
        appDependencies.createHabitsAppWidgetHabitIdsFeature(configId)
    }

    val title by habitsAppWidgetTitleFeature.state.collectAsState()
    val habitIds by habitsAppWidgetHabitIdsFeature.state.collectAsState()

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    onClick()
                }
        ) {
            Title(
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 16.dp,
                    end = 54.dp
                ),
                text = title.orEmpty().ifEmpty { "#$configId" }
            )

            Text(
                modifier = Modifier.padding(
                    start = 16.dp,
                    bottom = 16.dp,
                    end = 16.dp
                ),
                text = buildString {
                    val list = habitIds?.toList() ?: emptyList()
                    list.forEachIndexed { index, habitId ->
                        val habitNameFeature = rememberEpicStoreEntry {
                            appDependencies.createHabitNameFeature(habitId)
                        }
                        val habitName by habitNameFeature.state.collectAsState()
                        append(habitName)
                        if (index != list.lastIndex) {
                            appendLine()
                        }
                    }
                }
            )
        }
    }
}