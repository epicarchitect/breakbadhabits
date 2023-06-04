package epicarchitect.breakbadhabits.android.app

import android.appwidget.AppWidgetManager
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import epicarchitect.breakbadhabits.android.app.base.activity.ComposeActivity
import epicarchitect.breakbadhabits.di.holder.AppModuleHolder
import epicarchitect.breakbadhabits.foundation.uikit.effect.LaunchedEffectWhenExecuted
import epicarchitect.breakbadhabits.ui.habits.widgets.HabitsAppWidgetConfigCreation

class HabitsAppWidgetConfigCreationActivity : ComposeActivity() {

    override val themeResourceId = R.style.Activity

    @Composable
    override fun Content() {
        val presentationModule = AppModuleHolder.presentation.habits
        val widgetSystemId = remember {
            intent.extras!!.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID)
        }

        val viewModel = remember {
            presentationModule.createHabitWidgetCreationViewModel(widgetSystemId)
        }

        LaunchedEffectWhenExecuted(viewModel.creationController) {
            setResult(
                RESULT_OK,
                Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetSystemId)
            )
        }

        HabitsAppWidgetConfigCreation(
            titleInputController = viewModel.titleInputController,
            creationController = viewModel.creationController,
            habitsSelectionController = viewModel.habitsSelectionController
        )
    }
}