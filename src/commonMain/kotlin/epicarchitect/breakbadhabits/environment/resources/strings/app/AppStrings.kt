package epicarchitect.breakbadhabits.environment.resources.strings.app

import epicarchitect.breakbadhabits.environment.resources.strings.appDashboard.AppDashboardStrings
import epicarchitect.breakbadhabits.environment.resources.strings.appSettings.AppSettingsStrings
import epicarchitect.breakbadhabits.environment.resources.strings.durationFormat.DurationFormattingStrings
import epicarchitect.breakbadhabits.environment.resources.strings.habits.creation.HabitCreationStrings
import epicarchitect.breakbadhabits.environment.resources.strings.habits.dashboard.HabitDashboardStrings
import epicarchitect.breakbadhabits.environment.resources.strings.habits.editing.HabitEditingStrings
import epicarchitect.breakbadhabits.environment.resources.strings.habits.records.creation.HabitEventRecordCreationStrings
import epicarchitect.breakbadhabits.environment.resources.strings.habits.records.dashboard.HabitEventRecordsDashboardStrings
import epicarchitect.breakbadhabits.environment.resources.strings.habits.records.editing.HabitEventRecordEditingStrings
import epicarchitect.breakbadhabits.environment.resources.strings.habits.widgets.creation.HabitWidgetCreationStrings
import epicarchitect.breakbadhabits.environment.resources.strings.habits.widgets.dashboard.HabitWidgetsDashboardStrings
import epicarchitect.breakbadhabits.environment.resources.strings.habits.widgets.editing.HabitWidgetEditingStrings

interface AppStrings {
    val appSettingsStrings: AppSettingsStrings
    val appDashboardStrings: AppDashboardStrings
    val habitDashboardStrings: HabitDashboardStrings
    val habitCreationStrings: HabitCreationStrings
    val habitEditingStrings: HabitEditingStrings
    val habitEventRecordsDashboardStrings: HabitEventRecordsDashboardStrings
    val habitEventRecordCreationStrings: HabitEventRecordCreationStrings
    val habitEventRecordEditingStrings: HabitEventRecordEditingStrings
    val habitWidgetsDashboardStrings: HabitWidgetsDashboardStrings
    val habitWidgetCreationStrings: HabitWidgetCreationStrings
    val habitWidgetEditingStrings: HabitWidgetEditingStrings
    val durationFormattingStrings: DurationFormattingStrings
}