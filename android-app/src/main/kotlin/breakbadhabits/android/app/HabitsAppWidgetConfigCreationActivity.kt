package breakbadhabits.android.app

import android.appwidget.AppWidgetManager
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import breakbadhabits.android.app.ui.app.LocalPresentationModule
import breakbadhabits.android.app.ui.habits.HabitsAppWidgetConfigCreationScreen
import breakbadhabits.app.entity.HabitAppWidgetConfig
import breakbadhabits.foundation.controller.RequestController
import breakbadhabits.foundation.uikit.activity.ComposeActivity
import breakbadhabits.foundation.uikit.ext.collectState


class HabitsAppWidgetConfigCreationActivity : ComposeActivity() {

    override val themeResourceId = R.style.Activity

    @Composable
    override fun Content() {
        val presentationModule = BreakBadHabitsApp.instance.presentationModule
        val appWidgetId = remember {
            intent.extras!!.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID).toLong()
        }

        val viewModel = viewModel {
            presentationModule.createHabitAppWidgetCreationViewModel(
                HabitAppWidgetConfig.AppWidgetId(appWidgetId)
            )
        }

        val creationState by viewModel.creationController.collectState()

        LaunchedEffect(creationState) {
            if (creationState.requestState is RequestController.RequestState.Executed) {
                setResult(
                    RESULT_OK,
                    Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                )
                finish()
            }
        }

        HabitsAppWidgetConfigCreationScreen(
            titleInputController = viewModel.titleInputController,
            creationController = viewModel.creationController,
        )
    }
}