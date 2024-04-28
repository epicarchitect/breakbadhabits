package epicarchitect.breakbadhabits.habits.widget.android

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.compose.ui.text.intl.Locale
import epicarchitect.breakbadhabits.defaultDependencies.habits.widgets.creation.LocalizedHabitWidgetCreationResources
import epicarchitect.breakbadhabits.di.holder.AppModuleHolder
import epicarchitect.breakbadhabits.features.habits.widgets.creation.HabitWidgetCreationCreation
import epicarchitect.breakbadhabits.features.habits.widgets.creation.HabitWidgetCreationDependencies
import epicarchitect.breakbadhabits.features.habits.widgets.creation.HabitWidgetCreationNavigation
import epicarchitect.breakbadhabits.foundation.uikit.theme.AppColorsSchemes
import epicarchitect.breakbadhabits.foundation.uikit.theme.AppTheme

class HabitsAppWidgetConfigCreationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(
                colorScheme = AppColorsSchemes.light
            ) {
                val widgetSystemId = remember {
                    intent.extras!!.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID)
                }

                HabitWidgetCreationCreation(
                    dependencies = HabitWidgetCreationDependencies(
                        resources = LocalizedHabitWidgetCreationResources(Locale.current),
                        navigation = object : HabitWidgetCreationNavigation {
                            override fun back() {
                                setResult(
                                    RESULT_OK,
                                    Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetSystemId)
                                )
                                finish()
                            }
                        },
                        mainDatabase = AppModuleHolder.require().mainDatabase,
                        systemWidgetId = widgetSystemId,
                        idGenerator = AppModuleHolder.require().identification.idGenerator
                    )
                )
            }
        }
    }
}