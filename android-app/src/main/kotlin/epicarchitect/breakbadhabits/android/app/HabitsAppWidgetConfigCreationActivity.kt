package epicarchitect.breakbadhabits.android.app

import android.appwidget.AppWidgetManager
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import epicarchitect.breakbadhabits.android.app.base.activity.ComposeActivity
import epicarchitect.breakbadhabits.android.app.ui.habits.widgets.HabitsAppWidgetConfigCreationScreen
import epicarchitect.breakbadhabits.foundation.controller.SingleRequestController

class HabitsAppWidgetConfigCreationActivity : ComposeActivity() {

    override val themeResourceId = R.style.Activity

    @Composable
    override fun Content() {
        val presentationModule = BreakBadHabitsApp.instance.presentationModule
        val widgetSystemId = remember {
            intent.extras!!.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID)
        }

        val viewModel = viewModel {
            presentationModule.createHabitWidgetCreationViewModel(widgetSystemId)
        }

        val creationState by viewModel.creationController.state.collectAsState()

        LaunchedEffect(creationState) {
            if (creationState.requestState is SingleRequestController.RequestState.Executed) {
                setResult(
                    RESULT_OK,
                    Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetSystemId)
                )
                finish()
            }
        }

        HabitsAppWidgetConfigCreationScreen(
            titleInputController = viewModel.titleInputController,
            creationController = viewModel.creationController,
            habitsSelectionController = viewModel.habitsSelectionController
        )
    }
}