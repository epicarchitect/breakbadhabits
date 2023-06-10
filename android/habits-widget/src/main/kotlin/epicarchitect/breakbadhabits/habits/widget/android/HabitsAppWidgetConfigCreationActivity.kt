package epicarchitect.breakbadhabits.habits.widget.android

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import epicarchitect.breakbadhabits.di.holder.AppModuleHolder
import epicarchitect.breakbadhabits.foundation.uikit.effect.LaunchedEffectWhenExecuted
import epicarchitect.breakbadhabits.foundation.uikit.theme.AppColorsSchemes
import epicarchitect.breakbadhabits.foundation.uikit.theme.AppTheme

class HabitsAppWidgetConfigCreationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(
                colorScheme = AppColorsSchemes.light
            ) {
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
                    finish()
                }

                HabitsAppWidgetConfigCreation(
                    titleInputController = viewModel.titleInputController,
                    creationController = viewModel.creationController,
                    habitsSelectionController = viewModel.habitsSelectionController
                )
            }
        }
    }
}